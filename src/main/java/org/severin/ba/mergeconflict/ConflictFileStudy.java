package org.severin.ba.mergeconflict;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;
import org.severin.ba.mergeconflict.resolution.FileResolution;
import org.severin.ba.util.csv.CSVFile;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

@Command(name = "conflictfilestudy", mixinStandardHelpOptions = true)
public class ConflictFileStudy implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Parameters(index = "1") String logPath;
    @Option(names = {"-d", "--dir"}) String dir = "./";

    @Override
    public Integer call() throws Exception {
        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.listPath));

        CSVFile updater = new CSVFile(
                new File(logPath),
                new String[]{"projectName", "correctCount", "conflictingFileCount"},
                true
        );

        for (ProjectInfo projectInfo: reader) {
            System.out.format("Checking project %s\n", projectInfo.name);
            Project project = Project.buildFromPath(this.dir + "/" + projectInfo.name);

            try {
                ArrayList<Conflict> conflicts = project.getConflictingMerges();
                int conflictCount = conflicts.size();
                if (conflictCount > 500) {
                    System.out.println("FAIL:\tToo many Conflicts.\n");
                    continue;
                }

                System.out.format("Conflictcount: %d\n", conflictCount);

                int conflictingFileCount = 0;
                int correctCount = 0;

                for (Conflict conflict: conflicts) {
                    Map<String, ArrayList<FileResolution>> fileResolutions = conflict.buildFileResolutions();
                    Map<String, FileResolution> actualResolution = conflict.getActualFileResolutions();

                    if (ConflictFileStudy.mapsContainSameKeys(fileResolutions, actualResolution)) {
                        conflictingFileCount += actualResolution.size();
                        for (String fileName: actualResolution.keySet()) {
                            ArrayList<FileResolution> fileResolutionList = fileResolutions.get(fileName);
                            FileResolution actualFileResolution = actualResolution.get(fileName);

                            for (FileResolution fileResolution: fileResolutionList) {
                                if (actualFileResolution.compareTo(fileResolution) == 0) {
                                    correctCount++;
                                    break;
                                }
                            }
                        }
                    }
                }
                updater.appendRecord(projectInfo.name, correctCount, conflictingFileCount);
                System.out.format("OK:\t%d/%d conflicting files have correct solutions.\n\n", correctCount, conflictingFileCount);
            } catch (Exception e) {
                System.out.println("FAIL:\tProject could not be checked! " + e.getMessage() + "\n");
            }
            project.close();
        }
        return 0;
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
        int exitCode = new CommandLine(new ConflictFileStudy()).execute(args);
        System.exit(exitCode);
    }
}
