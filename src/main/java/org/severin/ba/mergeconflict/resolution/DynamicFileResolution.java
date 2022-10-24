package org.severin.ba.mergeconflict.resolution;

import org.eclipse.jgit.diff.RawText;
import org.severin.ba.util.path.Path;

public class DynamicFileResolution extends FileResolution {
    private final Path<String> path;

    public DynamicFileResolution(String fileName, Path<String> path) {
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
