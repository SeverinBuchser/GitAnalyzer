package org.severin.ba;

import org.severin.ba.api.Project;
import picocli.CommandLine.Command;

@Command(name = "mcr", mixinStandardHelpOptions = true, version = "MergeConflictResolution 0.1",
description = "")
public class Main {
    public static void main(String[] args) throws Exception {
        Project project = Project.buildFromPath(
                "Activiti/Activiti",
                "/home/severin/ba_projects"
        );

        project.close();
    }
}