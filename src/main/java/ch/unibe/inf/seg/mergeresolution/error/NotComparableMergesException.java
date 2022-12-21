package ch.unibe.inf.seg.mergeresolution.error;

/**
 * Exception if two resolution merges are incomparable.
 */
public class NotComparableMergesException extends Exception {
    public NotComparableMergesException() {
        super("The two resolutions are not comparable. One Resolution file may be a binary file!");
    }
}
