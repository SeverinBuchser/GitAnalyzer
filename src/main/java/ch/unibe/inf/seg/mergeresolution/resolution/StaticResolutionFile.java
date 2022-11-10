package ch.unibe.inf.seg.mergeresolution.resolution;

import org.eclipse.jgit.diff.RawText;

public class StaticResolutionFile extends ResolutionFile {

    private final byte[] content;

    public StaticResolutionFile(String fileName, byte[] input) {
        super(fileName);
        this.content = input;
    }

    @Override
    public RawText buildRawText() {
        return new RawText(this.content);
    }
}
