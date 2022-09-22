package org.severin.ba.merge;

import org.severin.ba.util.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConflictingMergeResolution extends ArrayList<ConflictingMergeFileResolution> {

    public ConflictingMergeResolution(List<ConflictingMergeFileResolution> mergeResolution) {
        super(mergeResolution);
    }

    public static ConflictingMergeResolution fromTreePath(Stack<Node<ConflictingMergeFileResolution>> path) {
        return new ConflictingMergeResolution(path.stream().map(Node::getItem).toList());
    }
}
