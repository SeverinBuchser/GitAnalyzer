package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.report.Report;

import java.io.OutputStream;
import java.io.PrintStream;

public class SimpleAnalyzerLogger extends PrintStream implements AnalyzerLogger {

    private final int levelZero;

    public SimpleAnalyzerLogger(OutputStream out, int levelZero) {
        super(out);
        this.levelZero = levelZero;
    }

    @Override
    public void println(Report report, int level) {
        if (level < this.levelZero) throw new IllegalArgumentException("The level has to be bigger or equal than the " +
                "level zero set at construction.");
        this.println(report.toString(level - this.levelZero));
    }
}
