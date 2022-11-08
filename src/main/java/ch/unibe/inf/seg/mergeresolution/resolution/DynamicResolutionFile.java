package ch.unibe.inf.seg.mergeresolution.resolution;

import ch.unibe.inf.seg.mergeresolution.util.path.Path;
import org.eclipse.jgit.diff.RawText;

public class DynamicResolutionFile extends ResolutionFile {
    private final Path<String> path;

    public DynamicResolutionFile(String fileName, Path<String> path) {
        super(fileName);
        this.path = path;
    }

    @Override
    protected RawText buildRawText() {
        StringBuilder builder = new StringBuilder();
        for (String chunk: this.path.build()) {
            builder.append(chunk);
        }

        byte[] bytes = builder.toString().getBytes();

        return new RawText(bytes);
    }

}
