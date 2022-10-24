package org.severin.ba.mergeconflict;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.Sequence;
import org.eclipse.jgit.merge.MergeChunk;
import org.eclipse.jgit.merge.MergeResult;
import org.severin.ba.mergeconflict.resolution.DynamicFileResolution;
import org.severin.ba.mergeconflict.resolution.FileResolution;
import org.severin.ba.util.path.Path;
import org.severin.ba.util.path.PathBuilder;

import java.util.ArrayList;
import java.util.List;


public class ConflictFile {
    private final MergeResult<? extends Sequence> mergeResult;
    private final String fileName;

    public ConflictFile(String fileName, MergeResult<? extends Sequence> mergeResult) {
        this.fileName = fileName;
        this.mergeResult = mergeResult;
    }

    private PathBuilder<String> buildResolutions() {
        PathBuilder<String> pathBuilder = new PathBuilder<>();
        List<? extends Sequence> sequenceList = this.mergeResult.getSequences();

        String firstRange = null;
        StringBuilder stringBuilder = new StringBuilder();

        for (MergeChunk mergeChunk: this.mergeResult) {
            Sequence sequence = sequenceList.get(mergeChunk.getSequenceIndex());

            if (!(sequence instanceof RawText)) continue;

            String chunk = ((RawText) sequence).getString(
                    mergeChunk.getBegin(),
                    mergeChunk.getEnd(),
                    false
            );

            switch (mergeChunk.getConflictState()) {
                case NO_CONFLICT -> stringBuilder.append(chunk);
                case FIRST_CONFLICTING_RANGE -> {
                    if (!stringBuilder.isEmpty()) {
                        pathBuilder.addItemLayer(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                    }
                    firstRange = chunk;
                }
                case NEXT_CONFLICTING_RANGE -> {
                    assert firstRange != null;

                    pathBuilder.addItemLayer(firstRange, chunk);
                }
            }
        }

        if (!stringBuilder.isEmpty()) {
            pathBuilder.addItemLayer(stringBuilder.toString());
        }

        return pathBuilder;
    }


    public ArrayList<FileResolution> getResolutions() {
        ArrayList<FileResolution> fileResolutions = new ArrayList<>();
        PathBuilder<String> paths = this.buildResolutions();
        if (paths.getPathCount() > 256) return null;
        for (int i = 0 ; i < paths.getPathCount() ; i++) {
            fileResolutions.add(new DynamicFileResolution(this.fileName, new Path<>(i, paths)));
        }
        return fileResolutions;
    }
}
