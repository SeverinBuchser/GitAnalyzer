package ch.unibe.inf.seg.gitanalyzer.util.subscription;

public interface Subscribable<T> {
    Subscription<T> subscribe(Subscriber<T> subscriber);
}
