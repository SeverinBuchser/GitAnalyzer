package org.severin.ba.merge;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "conflictstudy", mixinStandardHelpOptions = true)
public class ConflictStudy implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Option(names = {"-d", "--dir"}) String dir = "./";

    @Override
    public Integer call() throws Exception {
        ArrayList<ProjectInfo> projectInfoList = ProjectsInfoListReader.readListFile(this.listPath);

        int projectIndex = 1;
        int projectCount = projectInfoList.size();

        for (ProjectInfo projectInfo: projectInfoList) {
            try {
                System.out.format("Checking project %d out of %d: %s\n", projectIndex, projectCount, projectInfo.name);
                Project project = Project.buildFromPath(projectInfo.name, this.dir);

                ArrayList<ConflictingMerge> conflicts = project.getConflictingMerges();
                System.out.format("Conflicts: %d\n", conflicts.size());
                for (ConflictingMerge conflict: conflicts) {
                    ArrayList<ConflictingMergeResolution> resolutions = conflict.getResolutions();
                    ConflictingMergeResolution actualResolution = conflict.getActualResolution();

                    boolean hasCorrectOne = false;
                    for (ConflictingMergeResolution resolution: resolutions) {
                        if (actualResolution.compareTo(resolution) == 0) {
                            hasCorrectOne = true;
                            break;
                        }
                        ConflictingMergeResolution.diffFormat(System.out, resolution, actualResolution);
                    }

                    System.out.println(hasCorrectOne);
                }

                System.out.format("OK:\tProject %s checked\n\n", projectInfo.name);
                project.close();
            } catch (Exception e) {
                System.out.println("FAIL:\t Project " + projectInfo.name + " could not be checked! " + e.getMessage());
                e.printStackTrace();
            }
            projectIndex++;
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new org.severin.ba.merge.ConflictStudy()).execute(args);
        System.exit(exitCode);
    }
}
