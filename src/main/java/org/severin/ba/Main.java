package org.severin.ba;

import org.severin.ba.api.Project;
import org.severin.ba.merge.ConflictingMerge;
import org.severin.ba.merge.ConflictingMergeResolution;
import org.severin.ba.util.Formatting;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Project project = Project.buildFromPath(
                "4pr0n/ripme",
                "/home/severin/ba_projects"
        );
        ArrayList<ConflictingMerge> conflicts = project.getConflictingMerges();

        int conflictCount = 1;
        for (ConflictingMerge conflict: conflicts) {

            System.out.println("---------------------------------------------------------");
            System.out.format("Conflict: %d/%d\n", conflictCount, conflicts.size());
            System.out.format("Commit: %s\n", conflict.getCommitName());
            System.out.println("---------------------------------------------------------");

            ArrayList<ConflictingMergeResolution> mergeResolutions = conflict.getResolutions();
            ConflictingMergeResolution actualResolution = conflict.getActualResolution();

            int resolutionCount = 1;
            for (ConflictingMergeResolution mergeResolution: mergeResolutions) {

                System.out.println("---------------------------------------------------------");

                System.out.format("Resolution: %d/%d\n", resolutionCount, mergeResolutions.size());
                System.out.println("Differences: " + mergeResolution.compareTo(actualResolution));
                System.out.println("---------------------------------------------------------");
                ConflictingMergeResolution.diffFormat(
                        System.out,
                        //Integer.toString(resolutionCount),
                        mergeResolution,
                        actualResolution,
                        true
                );
                resolutionCount++;
            }
            System.out.println();
            System.out.println();
            conflictCount++;
        }



        project.close();
    }
}