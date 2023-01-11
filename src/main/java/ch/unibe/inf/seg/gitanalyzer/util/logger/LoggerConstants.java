package ch.unibe.inf.seg.gitanalyzer.util.logger;

public enum LoggerConstants {
    SEPARATOR('=');

    private static final int LENGTH = 150;
    private final String text;

    LoggerConstants(String text) {
        this.text = text;
    }

    LoggerConstants(char repeatable) {
        this.text = String.valueOf(repeatable).repeat(LENGTH);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
