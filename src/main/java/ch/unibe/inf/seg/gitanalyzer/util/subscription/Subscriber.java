package ch.unibe.inf.seg.gitanalyzer.util.subscription;

public interface Subscriber<T> {
    void next(T next);
}
