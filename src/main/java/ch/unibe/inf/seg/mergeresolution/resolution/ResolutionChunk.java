package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.merge.MergeChunk;

/**
 * A class representing a merge chunk.
 * The class is able to build a {@link RawText} object corresponding to the text of the merge chunk. Multiple merge
 * chunks can be concatenated to form a string of the text of every merge chunks.
 */
public class ResolutionChunk {
    /**
     * The merge chunk of this resolution chunk.
     */
    private final MergeChunk mergeChunk;
    /**
     * The sequence, the merge chunk belongs to.
     */
    private final RawText sequence;

    /**
     * Initates a new resolution chunk.
     * @param mergeChunk The merge chunk of the resolution chunk
     * @param sequence The sequence, the merge chunk belongs to.
     */
    public ResolutionChunk(MergeChunk mergeChunk, RawText sequence) {
        this.mergeChunk = mergeChunk;
        this.sequence = sequence;
    }

    /**
     * Builds the text of the merge chunk.
     * @return String belonging to the merge chunk.
     */
    public String getString() {
        return this.sequence.getString(
            this.mergeChunk.getBegin(),
            this.mergeChunk.getEnd(),
            false
        );
    }

    /**
     * Concatenates multiple resolution chunks in order to form a new string containing the text from both chunks.
     * @param chunks The chunks to concatenate.
     * @return The text of all the resolution chunks.
     */
    public static String concat(Iterable<ResolutionChunk> chunks) {
        StringBuilder builder = new StringBuilder();
        for (ResolutionChunk resolutionChunk: chunks) {
            builder.append(resolutionChunk.getString());
        }
        return builder.toString();
    }
}
