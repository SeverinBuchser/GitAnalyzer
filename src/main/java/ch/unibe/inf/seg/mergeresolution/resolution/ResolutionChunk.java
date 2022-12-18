package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.merge.MergeChunk;

public class ResolutionChunk {
    private final MergeChunk mergeChunk;
    private final RawText sequence;

    public ResolutionChunk(MergeChunk mergeChunk, RawText sequence) {
        this.mergeChunk = mergeChunk;
        this.sequence = sequence;
    }

    public String getString() {
        return this.sequence.getString(
            this.mergeChunk.getBegin(),
            this.mergeChunk.getEnd(),
            false
        );
    }

    public static String concat(Iterable<ResolutionChunk> chunks) {
        StringBuilder builder = new StringBuilder();
        for (ResolutionChunk resolutionChunk: chunks) {
            builder.append(resolutionChunk.getString());
        }
        return builder.toString();
    }
}
