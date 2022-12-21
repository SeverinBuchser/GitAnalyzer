package ch.unibe.inf.seg.mergeresolution.resolution;

import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import org.eclipse.jgit.diff.RawText;

/**
 * A dynamic resolution file is a file which can be build from a path of resolution chunks.
 */
public class DynamicResolutionFile extends ResolutionFile {
    /**
     * The path of resolution chunks.
     */
    private final Path<ResolutionChunk> path;

    /**
     * Initiates a new dynamic resolution file.
     * @param path The path of resolution chunks.
     */
    public DynamicResolutionFile(Path<ResolutionChunk> path) {
        super();
        this.path = path;
    }

    /**
     * Builds the raw text by concatenating all resolution chunks of the path and converting the resulting string into a
     * byte array.
     * @return The {@link RawText} of this resolution file.
     */
    @Override
    public RawText buildRawText() {
        return new RawText(ResolutionChunk.concat(this.path).getBytes());
    }

}
