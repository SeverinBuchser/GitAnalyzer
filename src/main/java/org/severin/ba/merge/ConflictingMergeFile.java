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


public class ConflictingMergeFile {
    private final MergeResult<? extends Sequence> mergeResult;
    private final String fileName;

    public ConflictingMergeFile(String fileName, MergeResult<? extends Sequence> mergeResult) {
        this.fileName = fileName;
        this.mergeResult = mergeResult;
    }

    private Tree<ResolutionChunk> buildResolutionTree() {
        Tree<ResolutionChunk> tree = new Tree<>();
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();

        Node<ResolutionChunk> firstConflictingRangeNode = null;
        ResolutionChunk lastResolutionChunk = null;

        for (MergeChunk chunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(chunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;

            RawResolutionChunk resolutionChunk = new RawResolutionChunk((RawText) sequence, chunk);

            switch (chunk.getConflictState()) {
                case NO_CONFLICT -> {
                    if (lastResolutionChunk == null) {
                        lastResolutionChunk = resolutionChunk;
                    } else {
                        lastResolutionChunk = new CombinedResolutionChunk(lastResolutionChunk, resolutionChunk);
                    }
                }
                case FIRST_CONFLICTING_RANGE -> {
                    if (lastResolutionChunk != null) {
                        tree.add(new Node<>(lastResolutionChunk));
                        lastResolutionChunk = null;
                    }
                    firstConflictingRangeNode = new Node<>(resolutionChunk);
                }
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstConflictingRangeNode != null;
                    tree.add(firstConflictingRangeNode, new Node<>(resolutionChunk));
                }
            }
        }

        return tree;
    }

    public ArrayList<ConflictingMergeFileResolution> getResolutions() {
        ArrayList<ConflictingMergeFileResolution> fileResolutions = new ArrayList<>();
        for (Stack<Node<ResolutionChunk>> path : this.buildResolutionTree().getPaths()) {
            fileResolutions.add(ConflictingMergeFileResolution.fromTreePath(this.fileName, path));
        }
        return fileResolutions;
    }
}
