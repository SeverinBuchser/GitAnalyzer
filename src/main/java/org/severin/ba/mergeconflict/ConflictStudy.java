package org.severin.ba.mergeconflict;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;
import org.severin.ba.mergeconflict.resolution.FileResolution;
import org.severin.ba.mergeconflict.resolution.Resolution;
import org.severin.ba.util.csv.CSVFile;
import org.severin.ba.util.path.Path;
import org.severin.ba.util.path.PathBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "conflictstudy", mixinStandardHelpOptions = true)
public class ConflictStudy implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Parameters(index = "1") String logPath;
    @Option(names = {"-d", "--dir"}) String dir = "./";

    @Override
    public Integer call() throws Exception {
        ProjectsInfoListReader reader = new ProjectsInfoListReader(new File(this.listPath));

        CSVFile updater = new CSVFile(
                new File(logPath),
                new String[]{"projectName", "correctCount", "conflictCount"},
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
                int correctCount = 0;
                for (Conflict conflict: conflicts) {
                    PathBuilder<FileResolution> resolutionTree = conflict.buildResolutions();
                    if (resolutionTree == null) {
                        conflictCount--;
                        continue;
                    }
                    Resolution actualResolution = conflict.getActualResolution();
                    for (Path<FileResolution> resolutionPath: resolutionTree) {
                        Resolution resolution = new Resolution(resolutionPath.build());
                        if (actualResolution.compareTo(resolution) == 0) {
                            correctCount++;
                            break;
                        }
                    }
                }
                updater.appendRecord(projectInfo.name, correctCount, conflictCount);
                System.out.format("OK:\t%d/%d conflicts have correct solutions.\n\n", correctCount, conflictCount);
            } catch (Exception e) {
                System.out.println("FAIL:\tProject could not be checked! " + e.getMessage() + "\n");
            }
            project.close();
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new ConflictStudy()).execute(args);
        System.exit(exitCode);
    }
}
