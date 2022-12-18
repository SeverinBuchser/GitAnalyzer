package ch.unibe.inf.seg.mergeresolution.error;

public class NotComparableMergesException extends Exception {
    public NotComparableMergesException() {
        super("The two resolutions are not comparable. They are not for the same merge!");
    }
}
