package org.severin.ba.mergeconflict.resolution;

import org.eclipse.jgit.diff.RawText;

public class StaticFileResolution extends FileResolution {

    private final byte[] content;

    public StaticFileResolution(String fileName, byte[] input) {
        super(fileName);
        this.content = input;
    }

    @Override
    protected RawText buildRawText() {
        return new RawText(this.content);
    }
}
