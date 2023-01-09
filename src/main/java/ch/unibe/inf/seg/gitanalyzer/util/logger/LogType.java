package ch.unibe.inf.seg.gitanalyzer.util.logger;

public enum LogType {
    SUCCESS("SUCCESS"),
    INFO("INFO"),
    FAIL("FAIL"),
    ERROR("ERROR");

    private final String longString;

    LogType(String longString) {
        this.longString = longString;
    }

    @Override
    public String toString() {
        return String.format("[%s]", this.longString);
    }
}
