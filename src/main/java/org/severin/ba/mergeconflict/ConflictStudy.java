package org.severin.ba.mergeconflict;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;
import org.severin.ba.mergeconflict.resolution.FileResolution;
import org.severin.ba.mergeconflict.resolution.Resolution;
import org.severin.ba.util.log.CSVFile;
import org.severin.ba.util.path.Path;
import org.severin.ba.util.path.PathBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "conflictstudy", mixinStandardHelpOptions = true)
public class ConflictStudy implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Parameters(index = "1") String logPath;
    @Option(names = {"-d", "--dir"}) String dir = "./";

    @Override
    public Integer call() throws Exception {
        ProjectsInfoListReader reader = new ProjectsInfoListReader(this.listPath);

        CSVFile updater = new CSVFile(
                logPath,
                new String[]{"projectName", "correct", "conflicts"},
                true
        );

        for (ProjectInfo projectInfo: reader) {
            try {
                System.out.format("Checking project %s\n", projectInfo.name);
                Project project = Project.buildFromPath(projectInfo.name, this.dir);

                ArrayList<Conflict> conflicts = project.getConflictingMerges();
                int conflictsSize = conflicts.size();
                if (conflictsSize > 500) {
                    project.close();
                    continue;
                }
                System.out.format("Conflictcount: %d\n", conflictsSize);
                int correctCount = 0;
                for (Conflict conflict: conflicts) {
                    try {
                        PathBuilder<FileResolution> resolutionTree = conflict.buildResolutions();
                        Resolution actualResolution = conflict.getActualResolution();
                        for (Path<FileResolution> resolutionPath: resolutionTree) {
                            Resolution resolution = new Resolution(resolutionPath.build());
                            try {
                                if (actualResolution.compareTo(resolution) == 0) {
                                    correctCount++;
                                    break;
                                }
                            } catch (Exception e) {}
                        }
                    } catch (NullPointerException e) {
                        conflictsSize--;
                        //System.out.println("Renames in:" + conflict.getCommitName());
                    }
                }
                updater.appendRecord(projectInfo.name, correctCount, conflictsSize);
                System.out.format("OK:\tProject %s checked. %d/%d have correct solutions.\n\n", projectInfo.name, correctCount, conflictsSize);
                project.close();
            } catch (Exception e) {
                System.out.println("FAIL:\t Project " + projectInfo.name + " could not be checked! " + e.getMessage());
                System.out.println();
            }
        }


        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new ConflictStudy()).execute(args);
        System.exit(exitCode);
    }
}
