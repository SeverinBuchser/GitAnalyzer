package org.severin.ba.autoclone;

import org.severin.ba.api.Project;
import org.severin.ba.api.ProjectInfo;
import org.severin.ba.api.ProjectsInfoListReader;

import java.util.ArrayList;

public class AutoCloneProjects {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            args = new String[2];
        }

        if (args[1] == null) {
            args[1] = "./";
        }

        ArrayList<ProjectInfo> projectInfoList = ProjectsInfoListReader.readList(args[0]);

        int projectIndex = 1;
        int projectCount = projectInfoList.size();

        for (ProjectInfo projectInfo: projectInfoList) {
            try {
                System.out.format("Cloning project %d out of %d: %s\n", projectIndex, projectCount, projectInfo.name);
                Project.cloneFromUri(projectInfo.name, projectInfo.uri, args[1]).close();
            } catch (Exception e) {
                System.out.println("Project " + projectInfo.name + " could not be cloned! " + e.getMessage());
            }
            projectIndex++;
        }

    }
}
