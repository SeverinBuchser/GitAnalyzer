package org.severin.ba.conflict;

import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.*;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Merge {

    protected final boolean canMerge;

    public RecursiveMerger getMerger() {
        return merger;
    }

    private final RecursiveMerger merger;

    public RevCommit getCommit() {
        return commit;
    }

    protected final RevCommit commit;

    public Merge(RevCommit commit, Repository repository) throws Exception {
        this.commit = commit;
        if (this.commit.getParents().length != 2) {
            throw new Exception("Object is not a regular merge!");
        }
        this.merger = (RecursiveMerger) MergeStrategy.RECURSIVE.newMerger(repository, true);
        this.canMerge = this.remerge();
    }

    private boolean remerge() throws IOException {
        RevCommit[] parents = this.commit.getParents();
        return this.merger.merge(
                parents[0].toObjectId(),
                parents[1].toObjectId()
        );
    }

    public void formatMerge(OutputStream out, String baseName, String oursName, String theirsName) throws IOException {
        MergeFormatter formatter = new MergeFormatter();
        Map<String, MergeResult<? extends Sequence>> results = this.merger.getMergeResults();

        for (MergeResult<? extends Sequence> result : results.values()) {
            formatter.formatMerge(out, result, baseName, oursName, theirsName, StandardCharsets.UTF_8);
        }
    }
}
