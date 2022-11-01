package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.Conflict;
import ch.unibe.inf.seg.mergeresolution.resolution.FileResolution;
import ch.unibe.inf.seg.mergeresolution.resolution.Resolution;
import ch.unibe.inf.seg.mergeresolution.project.Project;
import ch.unibe.inf.seg.mergeresolution.project.ProjectInfo;
import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import ch.unibe.inf.seg.mergeresolution.util.path.PathBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "analyze-resolutions", mixinStandardHelpOptions = true)
public class AnalyzeResolutions implements Callable<Integer> {

    @Parameters(index = "0") String projectListPath;
    @Parameters(index = "1") String resultsFileName;
    @Option(names = {"-d", "--dir"}) String projectsDir = "./";
    @Option(names = {"-f", "--files"}) boolean files = false;

    @Override
    public Integer call() throws Exception {
        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.projectListPath));

        CSVFile updater = new CSVFile(
                new File(resultsFileName),
                new String[]{"projectName", "correctCount", "totalCount"},
                true
        );

        for (ProjectInfo projectInfo: reader) {
            System.out.format("Checking project %s\n", projectInfo.name);
            Project project = Project.buildFromPath(this.projectsDir + "/" + projectInfo.name);

            try {
                ArrayList<Conflict> conflicts = project.getConflictingMerges();
                Integer conflictCount = conflicts.size();
                if (conflicts.size() > 500) {
                    System.out.println("FAIL:\tToo many Conflicts.\n");
                    continue;
                }

                System.out.format("Conflictcount: %d\n", conflictCount);

                Result result;
                if (this.files) {
                    result = this.analyzeConflictingFiles(conflicts);
                } else {
                    result = this.analyzeConflicts(conflicts);
                }

                updater.appendRecord(projectInfo.name, result.correctCount, result.totalCount);
                System.out.format("OK:\t%d/%d have correct solutions.\n\n", result.correctCount, result.totalCount);
            } catch (Exception e) {
                System.out.println("FAIL:\tProject could not be checked! " + e.getMessage() + "\n");
            }
            project.close();
        }
        return 0;
    }

    private Result analyzeConflicts(ArrayList<Conflict> conflicts) throws IOException {
        Result result = new Result(conflicts.size());
        
        for (Conflict conflict: conflicts) {
            PathBuilder<FileResolution> resolutionTree = conflict.buildResolutions();
            if (resolutionTree == null) {
                result.removeTotal();
                continue;
            }
            Resolution actualResolution = conflict.getActualResolution();
            for (Path<FileResolution> resolutionPath: resolutionTree) {
                Resolution resolution = new Resolution(resolutionPath.build());
                if (actualResolution.compareTo(resolution) == 0) {
                    result.addCorrect();
                    break;
                }
            }
        }
        return result;
    }

    private Result analyzeConflictingFiles(ArrayList<Conflict> conflicts) throws IOException {
        Result result = new Result(0);

        for (Conflict conflict: conflicts) {
            Map<String, ArrayList<FileResolution>> fileResolutions = conflict.buildFileResolutions();
            Map<String, FileResolution> actualResolution = conflict.getActualFileResolutions();

            if (AnalyzeResolutions.mapsContainSameKeys(fileResolutions, actualResolution)) {
                result.addTotal(actualResolution.size());
                for (String fileName: actualResolution.keySet()) {
                    ArrayList<FileResolution> fileResolutionList = fileResolutions.get(fileName);
                    FileResolution actualFileResolution = actualResolution.get(fileName);

                    for (FileResolution fileResolution: fileResolutionList) {
                        if (actualFileResolution.compareTo(fileResolution) == 0) {
                            result.addCorrect();
                            break;
                        }
                    }
                }
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
        int exitCode = new CommandLine(new AnalyzeResolutions()).execute(args);
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

        public void addCorrect() {
            this.correctCount++;
        }
    }
}
