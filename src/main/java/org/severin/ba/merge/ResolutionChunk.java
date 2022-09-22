package org.severin.ba.merge;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.merge.MergeChunk;

public class ResolutionChunk {
    private final RawText sequence;
    private final MergeChunk chunk;

    public ResolutionChunk(RawText sequence, MergeChunk chunk) {
        this.sequence = sequence;
        this.chunk = chunk;
    }

    public String getText() {
        return this.sequence.getString(this.chunk.getBegin(), this.chunk.getEnd(), false);
    }
}
