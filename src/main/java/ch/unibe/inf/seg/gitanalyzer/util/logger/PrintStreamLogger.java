package ch.unibe.inf.seg.gitanalyzer.util.logger;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class PrintStreamLogger implements Logger {

    private final PrintStream out;
    protected int verbosityLevel = 0;

    public void setVerbosityLevel(boolean[] verbosity) {
        this.verbosityLevel = verbosity.length;
    }

    public PrintStreamLogger(PrintStream out) {
        this.out = out;
    }

    @Override
    public void success(String text) {
        this.log(0, LogType.SUCCESS, text);
    }

    @Override
    public void success(int verbosityLevel, String text) {
        this.log(verbosityLevel, LogType.SUCCESS, text);
    }

    @Override
    public void info(String text) {
        this.log(0, LogType.INFO, text);
    }

    @Override
    public void info(int verbosityLevel, String text) {
        this.log(verbosityLevel, LogType.INFO, text);
    }

    @Override
    public void fail(String text) {
        this.log(0, LogType.FAIL, text);
    }

    @Override
    public void fail(int verbosityLevel, String text) {
        this.log(verbosityLevel, LogType.FAIL, text);
    }

    @Override
    public void error(String text) {
        this.log(0, LogType.ERROR, text);
    }

    private void log(int verbosityLevel, LogType logType, String text) {
        if (verbosityLevel > this.verbosityLevel) return;
        String prefix = logType + addTime();
        this.out.println(prefix + text);
    }

    private String addTime() {
        return " " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now()) + "\t";
    }
}
