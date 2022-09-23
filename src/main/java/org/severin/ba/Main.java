package org.severin.ba;

import org.severin.ba.api.Project;
import org.severin.ba.log.ConflictResolutionLogger;

public class Main {
    public static void main(String[] args) throws Exception {
        Project project = Project.buildFromPath(
                "4pr0n/ripme",
                "/home/severin/ba_projects"
        );

        ConflictResolutionLogger logger = new ConflictResolutionLogger(System.out);
        logger.logConflictsOfProject(project);

        project.close();
    }
}