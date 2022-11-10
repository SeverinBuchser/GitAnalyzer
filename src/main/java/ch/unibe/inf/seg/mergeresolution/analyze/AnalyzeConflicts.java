package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingChunk;
import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingFile;
import ch.unibe.inf.seg.mergeresolution.resolution.ResolutionFile;
import ch.unibe.inf.seg.mergeresolution.resolution.Resolution;
import ch.unibe.inf.seg.mergeresolution.project.Project;
import ch.unibe.inf.seg.mergeresolution.project.ProjectInfo;
import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import ch.unibe.inf.seg.mergeresolution.resolution.StaticResolutionFile;
import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import ch.unibe.inf.seg.mergeresolution.util.path.PathBuilder;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "analyze-conflicts", mixinStandardHelpOptions = true)
public class AnalyzeConflicts implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "The path to the project list CSV file."
    )
    String projectListPath;
    @Option(
            names = {"-pd", "--project-dir"},
            description = "The directory where the cloned projects are located."
    )
    String projectDir = ".";
    @Option(
            names = {"-od", "--out-dir"},
            description = "The output directory, where the output CSV file goes."
    )
    String outDir = ".";
    @Option(
            names = {"-m", "--mode"},
            description = "Analyze either 'merges', 'files' or 'chunks'."
    )
    String mode = "merges";
    @Option(
            names = {"-os", "--out-suffix"},
            description = "The suffix for the output CSV file."
    )
    String outSuffix = "";

    @Spec CommandSpec spec;

    private void validate() {
        switch (this.mode) {
            case "merges":
            case "files":
            case "chunks":
                break;
            default:
                throw new CommandLine.ParameterException(
                        spec.commandLine(),
                        "Invalid value for option 'mode': " + this.mode
                );
        }
    }

    private void normalizePaths() {
        this.projectListPath = FilenameUtils.separatorsToSystem(this.projectListPath);
        this.projectDir = FilenameUtils.separatorsToSystem(this.projectDir);
        this.outDir = FilenameUtils.separatorsToSystem(this.outDir);

        this.projectListPath = Paths.get(this.projectListPath).normalize().toAbsolutePath().toString();
        this.projectDir = Paths.get(this.projectDir).normalize().toAbsolutePath().toString();
        this.outDir = Paths.get(this.outDir).normalize().toAbsolutePath().toString();
    }

    private CSVFile getUpdater() throws IOException {
        String projectListFileName = FilenameUtils.getBaseName(this.projectListPath);
        return new CSVFile(
                Paths.get(this.outDir, projectListFileName + "-" + this.mode + this.outSuffix + ".csv").toFile(),
                new String[]{"projectName", "correctCount", "totalCount"},
                true
        );
    }

    private Project getProject(String projectName) throws IOException {
        return Project.buildFromPath(Paths.get(this.projectDir, FilenameUtils.separatorsToSystem(projectName)));
    }



    @Override
    public Integer call() throws Exception {
        this.validate();
        this.normalizePaths();

        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.projectListPath));
        CSVFile updater = this.getUpdater();

        for (ProjectInfo projectInfo: reader) {
            System.out.format("Checking project %s\n", projectInfo.name);
            Project project = this.getProject(projectInfo.name);

            try {
                ArrayList<ConflictingMerge> conflictingMerges = project.getConflictingMerges();
                Integer conflictCount = conflictingMerges.size();
                if (conflictingMerges.size() > 500) {
                    System.out.println("FAIL:\tToo many Conflicts.\n");
                    continue;
                }

                System.out.format("Conflictcount: %d\n", conflictCount);

                Result result = switch (this.mode) {
                    case "files" -> this.analyzeConflictingFiles(conflictingMerges);
                    case "chunks" -> this.analyzeConflictingChunks(conflictingMerges);
                    default -> this.analyzeConflictingMerges(conflictingMerges);
                };

                updater.appendRecord(projectInfo.name, result.correctCount, result.totalCount);
                System.out.format("OK:\t%d/%d have correct solutions.\n\n", result.correctCount, result.totalCount);
            } catch (Exception e) {
                System.out.println("FAIL:\tProject could not be checked! " + e.getMessage() + "\n");
            }
            project.close();
        }
        return 0;
    }

    private Result analyzeConflictingMerges(ArrayList<ConflictingMerge> conflictingMerges) throws IOException {
        Result result = new Result(conflictingMerges.size());
        
        for (ConflictingMerge conflictingMerge : conflictingMerges) {
            PathBuilder<ResolutionFile> resolutionTree = conflictingMerge.buildResolutions();
            if (resolutionTree == null) {
                result.removeTotal();
                continue;
            }
            Resolution actualResolution = conflictingMerge.getActualResolution();
            for (Path<ResolutionFile> resolutionPath: resolutionTree) {
                Resolution resolution = new Resolution(resolutionPath.build());
                if (actualResolution.compareTo(resolution) == 0) {
                    result.addCorrect();
                    break;
                }
            }
        }
        return result;
    }

    private Result analyzeConflictingFiles(ArrayList<ConflictingMerge> conflictingMerges) throws IOException {
        Result result = new Result(0);

        for (ConflictingMerge conflictingMerge : conflictingMerges) {
            Map<String, ArrayList<ResolutionFile>> fileResolutions = conflictingMerge.buildFileResolutions();
            Map<String, ResolutionFile> actualResolution = conflictingMerge.getActualFileResolutions();

            if (AnalyzeConflicts.mapsContainSameKeys(fileResolutions, actualResolution)) {
                result.addTotal(actualResolution.size());
                for (String fileName: actualResolution.keySet()) {
                    ArrayList<ResolutionFile> resolutionFileList = fileResolutions.get(fileName);
                    ResolutionFile actualResolutionFile = actualResolution.get(fileName);

                    for (ResolutionFile resolutionFile : resolutionFileList) {
                        if (actualResolutionFile.compareTo(resolutionFile) == 0) {
                            result.addCorrect();
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    private Result analyzeConflictingChunks(ArrayList<ConflictingMerge> conflictingMerges) throws IOException {
        Result result = new Result(0);

        for (ConflictingMerge conflictingMerge : conflictingMerges) {
            Map<String, ConflictingFile> conflictingFiles = conflictingMerge.getConflictingFiles();

            for (ConflictingFile conflictingFile: conflictingFiles.values()) {
                ArrayList<ConflictingChunk> conflictingChunks = conflictingFile.getConflictingChunks();
                StaticResolutionFile actualResolutionFile = conflictingFile.getActualResolutionFile();

                ArrayList<Boolean> foundCorrectResolutions = new ArrayList<>(Collections.nCopies(
                        conflictingFile.getConflictCount(),
                        false
                ));

                int chunkIndex = 0;
                for (ConflictingChunk conflictingChunk : conflictingChunks) {
                    if (conflictingChunk.isFirstConflictingRangeInResolutionFile(actualResolutionFile)) {
                        foundCorrectResolutions.set(chunkIndex, true);
                        chunkIndex++;
                        continue;
                    }

                    if (conflictingChunk.isNextConflictingRangeInResolutionFile(actualResolutionFile)) {
                        foundCorrectResolutions.set(chunkIndex, true);
                    }
                    chunkIndex++;
                }

                result.addTotal(conflictingFile.getConflictCount());
                result.addCorrect(Collections.frequency(foundCorrectResolutions, true));
            }
        }

        return result;
    }

    private static boolean mapsContainSameKeys(Map<String, ?> map1, Map<String, ?> map2) {
        if (map1 == null || map2 == null) return false;
        List<String> map1Keys = new ArrayList<>(map1.keySet());
        List<String> map2Keys = new ArrayList<>(map2.keySet());

        Collections.sort(map1Keys);
        Collections.sort(map2Keys);

        return map1Keys.equals(map2Keys);
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new AnalyzeConflicts()).execute(args);
        System.exit(exitCode);
    }
    
    private static class Result {
        public int totalCount;
        public int correctCount = 0;

        Result(int totalCount) {
            this.totalCount = totalCount;
        }

        public void removeTotal() {
            this.totalCount--;
        }

        public void addTotal(int toAdd) {
            this.totalCount += toAdd;
        }

        public void addCorrect(int toAdd) {
            this.correctCount += toAdd;
        }
        public void addCorrect() {
            this.correctCount++;
        }
    }
}
