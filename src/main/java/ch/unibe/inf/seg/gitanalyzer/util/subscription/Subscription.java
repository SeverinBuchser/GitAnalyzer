package ch.unibe.inf.seg.gitanalyzer.util.subscription;

public class Subscription<T> {

    private final SubscriptionManager<T> subscriptionManager;
    private final Subscriber<T> subscriber;

    public Subscription(SubscriptionManager<T> subscriptionManager, Subscriber<T> subscriber) {
        this.subscriptionManager = subscriptionManager;
        this.subscriber = subscriber;
    }

    public void quit() {
        this.subscriptionManager.remove(this);
    }

    public void next(T next) {
        this.subscriber.next(next);
    }
}
