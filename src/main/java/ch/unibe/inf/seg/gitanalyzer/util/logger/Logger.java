package ch.unibe.inf.seg.gitanalyzer.util.logger;

public interface Logger {
    void success(String text);
    void success(int verbosityLevel, String text);
    void info(String text);
    void info(int verbosityLevel, String text);
    void fail(String text);
    void fail(int verbosityLevel, String text);
    void error(String text);
}
