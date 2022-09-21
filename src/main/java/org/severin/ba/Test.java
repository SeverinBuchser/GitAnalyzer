package org.severin.ba;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.merge.MergeResult;
import org.severin.ba.conflict.Merge;
import org.severin.ba.project.Project;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException, GitAPIException {
        Project project = Project.buildFromPath("test", "/home/severin/gitrepo/Severin/UniBe");
        Iterable<Merge> conflicts = project.getConflicts();
        for (Merge conflict: conflicts) {
            Map<String, MergeResult<? extends Sequence>> results = conflict.getMerger().getMergeResults();
            for (Map.Entry<String, MergeResult<? extends Sequence>> result: results.entrySet()) {
                System.out.println(result.getKey());
                File file = new File(project.getRepository().getWorkTree(), result.getKey());
                RawText text = new RawText(file);

                System.out.println(text.getString(0));

                System.out.println(((RawText) result.getValue().getSequences().get(1)).getString(0));
            }
        }

        project.close();
    }
}

