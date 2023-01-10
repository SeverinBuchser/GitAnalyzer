package ch.unibe.inf.seg.gitanalyzer.util.logger;

import java.io.PrintStream;

public class GuiLogger extends PrintStreamLogger {
    private final StringBuilderOutputStream outputStream = new OutputStreamDuplicator(System.out);

    public StringBuilderOutputStream getOutputStream() {
        return this.outputStream;
    }

    public GuiLogger() {
        this.out = new PrintStream(this.outputStream);
    }
}
