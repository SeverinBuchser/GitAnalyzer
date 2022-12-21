package ch.unibe.inf.seg.gitanalyzer.helper;

import java.io.OutputStream;
import java.io.PrintStream;

public class TestingPrintStream extends PrintStream {
    public TestingPrintStream() {
        super(new OutputStream() {
            @Override
            public void write(int b) {}
        });
    }
}
