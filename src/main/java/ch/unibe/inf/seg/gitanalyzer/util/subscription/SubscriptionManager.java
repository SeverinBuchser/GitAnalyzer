package ch.unibe.inf.seg.gitanalyzer.util.subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionManager<T> {
    private final List<Subscription<T>> subscriptions = new ArrayList<>();

    public void add(Subscription<T> subscription) {
        this.subscriptions.add(subscription);
    }

    public void remove(Subscription<T> subscription) {
        this.subscriptions.remove(subscription);
    }

    public void nextAll(T next) {
        this.subscriptions.forEach(sub -> sub.next(next));
    }

    public Subscription<T> create(Subscriber<T> subscriber) {
        Subscription<T> subscription = new Subscription<>(this, subscriber);
        this.add(subscription);
        return subscription;
    }
}
