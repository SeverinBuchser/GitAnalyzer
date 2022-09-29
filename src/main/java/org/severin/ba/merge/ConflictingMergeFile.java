package org.severin.ba.merge;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.merge.MergeChunk;
import org.eclipse.jgit.merge.MergeResult;
import org.severin.ba.util.tree.Node;
import org.severin.ba.util.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class ConflictingMergeFile extends Tree<ResolutionChunk> {
    private final MergeResult<? extends Sequence> mergeResult;
    private final String fileName;

    public ConflictingMergeFile(String fileName, MergeResult<? extends Sequence> mergeResult) {
        this.fileName = fileName;
        this.mergeResult = mergeResult;
        this.buildResolutionTree();
    }

    private void buildResolutionTree() {
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();

        Node<ResolutionChunk> firstConflictingRangeNode = null;

        for (MergeChunk chunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(chunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;

            Node<ResolutionChunk> newNode = new Node<>(new ResolutionChunk((RawText) sequence, chunk));

            switch (chunk.getConflictState()) {
                case NO_CONFLICT -> this.add(newNode);
                case FIRST_CONFLICTING_RANGE -> firstConflictingRangeNode = newNode;
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstConflictingRangeNode != null;
                    this.add(firstConflictingRangeNode, newNode);
                }
            }
        }
    }

    public ArrayList<ConflictingMergeFileResolution> getResolutions() {
        ArrayList<ConflictingMergeFileResolution> fileResolutions = new ArrayList<>();
        for (Stack<Node<ResolutionChunk>> path : this.getPaths()) {
            fileResolutions.add(ConflictingMergeFileResolution.fromTreePath(this.fileName, path));
        }
        return fileResolutions;
    }
}
