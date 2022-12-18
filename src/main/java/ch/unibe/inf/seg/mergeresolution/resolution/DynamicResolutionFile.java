package ch.unibe.inf.seg.mergeresolution.resolution;

import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import org.eclipse.jgit.diff.RawText;

public class DynamicResolutionFile extends ResolutionFile {
    private final Path<ResolutionChunk> path;

    public DynamicResolutionFile(String fileName, Path<ResolutionChunk> path) {
        super(fileName);
        this.path = path;
    }

    @Override
    public RawText buildRawText() {
        return new RawText(ResolutionChunk.concat(this.path).getBytes());
    }

}
