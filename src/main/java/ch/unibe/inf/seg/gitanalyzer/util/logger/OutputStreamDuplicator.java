package ch.unibe.inf.seg.gitanalyzer.util.logger;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamDuplicator extends StringBuilderOutputStream {
    private final OutputStream old;

    public OutputStreamDuplicator(OutputStream old) {
        this.old = old;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        this.old.write(b);
    }
}
