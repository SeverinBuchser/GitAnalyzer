package ch.unibe.inf.seg.gitanalyzer.resolution;

import org.eclipse.jgit.diff.RawText;

/**
 * A static resolution file is a resolution file build from a byte array.
 * The file can be created using only a byte array in order to create the {@link RawText}.
 */
public class StaticResolutionFile extends ResolutionFile {

    /**
     * The content of the resolution file.
     */
    private final byte[] content;

    /**
     * Initiates a new static resolution file.
     * @param input The content of the resolution file in byte form.
     */
    public StaticResolutionFile(byte[] input) {
        super();
        this.content = input;
    }

    /**
     *  Builds a raw text from the {@link #content}.
     * @return The {@link RawText} of this resolution file.
     */
    @Override
    public RawText buildRawText() {
        return new RawText(this.content);
    }
}
