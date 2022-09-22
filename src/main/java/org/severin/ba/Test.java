package org.severin.ba;

import org.severin.ba.api.Project;
import org.severin.ba.merge.ConflictingMerge;
import org.severin.ba.merge.ConflictingMergeResolution;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws Exception {
        Project project = Project.buildFromPath(
                "4pr0n/ripme",
                "/home/severin/ba_projects"
        );
        Iterable<ConflictingMerge> conflicts = project.getConflictingMerges();
        for (ConflictingMerge conflict: conflicts) {
            ArrayList<ConflictingMergeResolution> mergeResolutions = conflict.getMergeResolutions();
        }

//            if (fileResolutions.size() >= 2) {
//                System.out.println("---------------------------------");
//                System.out.println(entry.getKey());
//                System.out.format("Paths: %d\n", fileResolutions.size());
//                System.out.println("---------------------------------");
//                ConflictingMergeFileResolution.diffFormat(System.out, fileResolutions.get(0), fileResolutions.get(1));
//                System.out.println();
//                System.out.println();
//                System.out.println();
//            }

        project.close();
    }
}

