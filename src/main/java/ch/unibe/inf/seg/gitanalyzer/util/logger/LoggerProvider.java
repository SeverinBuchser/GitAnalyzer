package ch.unibe.inf.seg.gitanalyzer.util.logger;

import picocli.CommandLine;

public class LoggerProvider implements Logger {
    private static final PrintStreamLogger logger = new PrintStreamLogger(System.out);

    public static PrintStreamLogger getLogger() {
        return logger;
    }

    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "Increase verbosity. Specify multiple times to increase (-vvv)."
    )
    public void setVerbosityLevel(boolean[] verbosity) {
        this.setVerbosityLevel(verbosity.length);
    }

    @Override
    public void success(String text) {
        logger.success(text);
    }

    @Override
    public void success(int verbosityLevel, String text) {
        logger.success(verbosityLevel, text);
    }

    @Override
    public void info(String text) {
        logger.info(text);
    }

    @Override
    public void info(int verbosityLevel, String text) {
        logger.info(verbosityLevel, text);
    }

    @Override
    public void fail(String text) {
        logger.fail(text);
    }

    @Override
    public void fail(int verbosityLevel, String text) {
        logger.fail(verbosityLevel, text);
    }

    @Override
    public void error(String text) {
        logger.error(text);
    }

    @Override
    public void separator() {
        logger.separator();
    }

    @Override
    public void separator(int verbosityLevel) {
        logger.separator(verbosityLevel);
    }

    @Override
    public void setVerbosityLevel(int verbosityLevel) {
        logger.setVerbosityLevel(verbosityLevel);
    }
}
