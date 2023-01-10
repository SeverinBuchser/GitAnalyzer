package ch.unibe.inf.seg.gitanalyzer.util.logger;

public class LoggerProvider {
    private static Logger logger = new GlobalLogger();

    public static void setLogger(Logger logger) {
        LoggerProvider.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }
}
