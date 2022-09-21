package org.severin.ba;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.severin.ba.conflict.Merge;
import org.severin.ba.project.Project;

import java.io.IOException;

public class FindConflicts {
    public static void main(String[] args) throws IOException, GitAPIException {
        Project project = Project.buildFromPath(
                "4pr0n/ripme",
                "/home/severin/ba_projects"
        );
        Iterable<Merge> conflicts = project.getConflicts();


        for (Merge conflict: conflicts) {
            System.out.println(conflict.getCommit().toObjectId());
            conflict.formatMerge(System.out, "a", "b", "c");
        }

        project.close();
    }
}

