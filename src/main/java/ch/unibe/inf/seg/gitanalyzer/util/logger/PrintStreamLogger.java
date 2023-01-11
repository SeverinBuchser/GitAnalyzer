package ch.unibe.inf.seg.gitanalyzer.util.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class PrintStreamLogger implements Logger {

    private static final int LOG_TYPE_LENGTH = 9;

    protected PrintStream out;

    public PrintStream getOut() {
        return out;
    }

    protected int verbosityLevel = 0;

    public void setVerbosityLevel(int verbosityLevel) {
        this.verbosityLevel = verbosityLevel;
    }

    public int getVerbosityLevel() {
        return this.verbosityLevel;
    }

    public PrintStreamLogger(OutputStream out) {
        this(new PrintStream(out));
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

    @Override
    public void separator() {
        this.info(LoggerConstants.SEPARATOR.toString());
    }

    @Override
    public void separator(int verbosityLevel) {
        this.info(verbosityLevel, LoggerConstants.SEPARATOR.toString());
    }

    private void log(int verbosityLevel, LogType logType, String text) {
        if (verbosityLevel > this.verbosityLevel) return;
        for (String line: text.split("\n")) {
            String prefix = addLogType(logType) + addTime();
            this.out.println(prefix + line);
        }
    }

    private static String addLogType(LogType logType) {
        return logType.toString() + " ".repeat(LOG_TYPE_LENGTH - logType.toString().length());
    }

    private static String addTime() {
        return " " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now()) + "\t";
    }
}
