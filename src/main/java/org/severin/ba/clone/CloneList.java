package org.severin.ba.clone;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@Command(name = "clonelist", mixinStandardHelpOptions = true)
public class CloneList implements Callable<Integer> {

    @Parameters(index = "0") String listPath;
    @Option(names = {"-d", "--destination"}) String destination = "./";

    @Override
    public Integer call() throws Exception {
        ArrayList<ProjectInfo> projectInfoList = ProjectsInfoListReader.readListFile(this.listPath);

        int projectIndex = 1;
        int projectCount = projectInfoList.size();

        for (ProjectInfo projectInfo: projectInfoList) {
            try {
                System.out.format("Cloning project %d out of %d: %s\n", projectIndex, projectCount, projectInfo.name);
                Project.cloneFromUri(projectInfo.name, projectInfo.uri, this.destination).close();
                System.out.format("Project %s cloned from %s to '%s'\n", projectInfo.name, projectInfo.uri, this.destination);
            } catch (Exception e) {
                System.out.println("Project " + projectInfo.name + " could not be cloned! " + e.getMessage());
            }
            projectIndex++;
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new CloneList()).execute(args);
        System.exit(exitCode);
    }
}
