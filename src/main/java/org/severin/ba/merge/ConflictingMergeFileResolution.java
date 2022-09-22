package org.severin.ba.merge;

import org.eclipse.jgit.diff.*;
import org.severin.ba.util.Node;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

public class ConflictingMergeFileResolution extends RawText {


    private final String fileName;

    public ConflictingMergeFileResolution(String fileName, byte[] input) {
        super(input);
        this.fileName = fileName;
    }

    public static ConflictingMergeFileResolution fromTreePath(String fileName, Stack<Node<ResolutionChunk>> path) {
        StringBuilder builder = new StringBuilder();
        while (!path.isEmpty()) {
            builder.append(path.pop().getItem().getText());
        }
       return new ConflictingMergeFileResolution(fileName, builder.toString().getBytes());
    }

    public static EditList diff(ConflictingMergeFileResolution resolution1, ConflictingMergeFileResolution resolution2) {
        DiffAlgorithm da = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS);
        SequenceComparator<RawText> sc = RawTextComparator.DEFAULT;
        return da.diff(sc, resolution1, resolution2);
    }

    public static void diffFormat(
            OutputStream out,
            ConflictingMergeFileResolution resolution1,
            ConflictingMergeFileResolution resolution2
    ) throws IOException {
        EditList el = ConflictingMergeFileResolution.diff(resolution1, resolution2);

        DiffFormatter df = new DiffFormatter(out);
        df.format(el, resolution1, resolution2);
    }
}
