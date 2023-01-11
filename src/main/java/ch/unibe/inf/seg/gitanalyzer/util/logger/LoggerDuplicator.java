package ch.unibe.inf.seg.gitanalyzer.util.logger;

public class LoggerDuplicator implements Logger {
    private Logger outOne;
    private Logger outTwo;

    public void setOutOne(Logger outOne) {
        this.outOne = outOne;
    }

    public void setOutTwo(Logger outTwo) {
        this.outTwo = outTwo;
    }

    protected LoggerDuplicator() {}

    public LoggerDuplicator(Logger outOne, Logger outTwo) {
        this.outOne = outOne;
        this.outTwo = outTwo;
    }

    @Override
    public void success(String text) {
        this.outOne.success(text);
        this.outTwo.success(text);
    }

    @Override
    public void success(int verbosityLevel, String text) {
        this.outOne.success(verbosityLevel, text);
        this.outTwo.success(verbosityLevel, text);
    }

    @Override
    public void info(String text) {
        this.outOne.info(text);
        this.outTwo.info(text);
    }

    @Override
    public void info(int verbosityLevel, String text) {
        this.outOne.info(verbosityLevel, text);
        this.outTwo.info(verbosityLevel, text);
    }

    @Override
    public void fail(String text) {
        this.outOne.fail(text);
        this.outTwo.fail(text);
    }

    @Override
    public void fail(int verbosityLevel, String text) {
        this.outOne.fail(verbosityLevel, text);
        this.outTwo.fail(verbosityLevel, text);
    }

    @Override
    public void error(String text) {
        this.outOne.error(text);
        this.outTwo.error(text);
    }

    @Override
    public void separator() {
        this.outOne.separator();
        this.outTwo.separator();
    }

    @Override
    public void separator(int verbosityLevel) {
        this.outOne.separator(verbosityLevel);
        this.outTwo.separator(verbosityLevel);
    }

    @Override
    public void setVerbosityLevel(int verbosityLevel) {
        this.outOne.setVerbosityLevel(verbosityLevel);
        this.outTwo.setVerbosityLevel(verbosityLevel);
    }
}
