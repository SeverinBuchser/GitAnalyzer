package org.severin.ba.merge;

import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.severin.ba.util.Node;
import org.severin.ba.util.Tree;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class ConflictingMerge extends Tree<ConflictingMergeFileResolution> {
    protected final RecursiveMerger merger;

    protected final RevCommit commit;

    public ConflictingMerge(RevCommit commit, RecursiveMerger merger) {
        this.commit = commit;
        this.merger = merger;
        this.buildResolutionTree();
    }

    private void buildResolutionTree() {
        for (Map.Entry<String, MergeResult<? extends Sequence>> entry: this.merger.getMergeResults().entrySet()) {

            ConflictingMergeFile conflictingFile = new ConflictingMergeFile(entry.getKey(), entry.getValue());
            ArrayList<ConflictingMergeFileResolution> fileResolutions = conflictingFile.getFileResolutions();
            ArrayList<Node<ConflictingMergeFileResolution>> fileResolutionsNodes = new ArrayList<>();

            for (ConflictingMergeFileResolution fileResolution: fileResolutions) {
                fileResolutionsNodes.add(new Node<>(fileResolution));
            }

            this.addList(fileResolutionsNodes);
        }
    }

    public ArrayList<ConflictingMergeResolution> getMergeResolutions() {
        ArrayList<ConflictingMergeResolution> mergeResolutions = new ArrayList<>();
        for (Stack<Node<ConflictingMergeFileResolution>> path : this.getPaths()) {
            mergeResolutions.add(ConflictingMergeResolution.fromTreePath(path));
        }
        return mergeResolutions;
    }
}
