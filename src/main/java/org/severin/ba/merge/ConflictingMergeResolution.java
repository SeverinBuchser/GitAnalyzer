package org.severin.ba.merge;

import org.eclipse.jgit.diff.EditList;
import org.severin.ba.util.Formatting;
import org.severin.ba.util.Node;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class ConflictingMergeResolution extends ArrayList<ConflictingMergeFileResolution> implements Comparable<ConflictingMergeResolution> {

    public ConflictingMergeResolution(List<ConflictingMergeFileResolution> mergeResolution) {
        super(mergeResolution);
    }

    private Optional<ConflictingMergeFileResolution> findOptionalByFileName(String fileName) {
        return this.stream().filter(fileResolution -> fileResolution.getFileName().equals(fileName)).findAny();
    }

    public ConflictingMergeFileResolution getByFileName(String fileName) {
        Optional<ConflictingMergeFileResolution> optionalFileResolution = this.findOptionalByFileName(fileName);
        return optionalFileResolution.orElse(null);
    }

    public static ConflictingMergeResolution fromTreePath(Stack<Node<ConflictingMergeFileResolution>> path) {
        return new ConflictingMergeResolution(path.stream().map(Node::getItem).toList());
    }

    public static ArrayList<EditList> diff(
            ConflictingMergeResolution resolution1,
            ConflictingMergeResolution resolution2
    ) throws Exception {
        if (resolution1.doesNotHaveSameFiles(resolution2)) {
            throw new Exception("The two resolutions are not comparable. They are not for the same merge!");
        }
        ArrayList<EditList> edits = new ArrayList<>();

        for (ConflictingMergeFileResolution fileResolution1: resolution1) {
            ConflictingMergeFileResolution fileResolution2 = resolution2.getByFileName(fileResolution1.getFileName());
            edits.add(ConflictingMergeFileResolution.diff(fileResolution1, fileResolution2));
        }

        return edits;
    }

    public static void diffFormat(
            OutputStream out,
            ConflictingMergeResolution resolution1,
            ConflictingMergeResolution resolution2
    ) throws Exception {
        ConflictingMergeResolution.diffFormat(out, resolution1, resolution2, false);
    }

    public static void diffFormat(
            OutputStream out,
            ConflictingMergeResolution resolution1,
            ConflictingMergeResolution resolution2,
            boolean formatDifferences
    ) throws Exception {
        if (resolution1.doesNotHaveSameFiles(resolution2)) {
            throw new Exception("The two resolutions are not comparable. They are not for the same merge!");
        }

        ConflictingMergeResolution.formatHead(out, resolution1, resolution2, formatDifferences);

        for (ConflictingMergeFileResolution fileResolution1: resolution1) {
            ConflictingMergeFileResolution fileResolution2 = resolution2.getByFileName(fileResolution1.getFileName());
            ConflictingMergeFileResolution.diffFormat(out, fileResolution1, fileResolution2);
        }
    }

    private static void formatHead(
            OutputStream out,
            ConflictingMergeResolution resolution1,
            ConflictingMergeResolution resolution2,
            boolean formatDifferences
    ) throws IOException {
        if (formatDifferences) {
        }
    }

    public static void diffLog(
            OutputStream out,
            String identifier,
            ConflictingMergeResolution resolution1,
            ConflictingMergeResolution resolution2
    ) throws Exception {
        int differences = resolution1.compareTo(resolution2);
        out.write(identifier.getBytes(Charset.defaultCharset()));
        out.write(Formatting.SEMICOLON.getBytes());
        out.write(Integer.toString(differences).getBytes(Charset.defaultCharset()));
        out.write(Formatting.NEW_LINE.getBytes());
    }

    public boolean doesNotHaveSameFiles(ConflictingMergeResolution resolution) {
        if (this.size() != resolution.size()) return true;
        for (ConflictingMergeFileResolution fileResolution: this) {
            if (resolution.findOptionalByFileName(fileResolution.getFileName()).isEmpty()) return true;
        }
        return false;
    }

    @Override
    public int compareTo(ConflictingMergeResolution otherResolution) {
        try {
            ArrayList<EditList> edits = ConflictingMergeResolution.diff(this, otherResolution);
            int editCount = 0;

            for (EditList edit: edits) {
                editCount += edit.size();
            }

            return editCount;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
