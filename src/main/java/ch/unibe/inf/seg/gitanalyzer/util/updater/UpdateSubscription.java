package ch.unibe.inf.seg.gitanalyzer.util.updater;

public class UpdateSubscription {

    private final UpdateSubscriptionManager updateSubscriptionManager;
    private final Updatable updatable;

    public UpdateSubscription(UpdateSubscriptionManager updateSubscriptionManager, Updatable updatable) {
        this.updateSubscriptionManager = updateSubscriptionManager;
        this.updatable = updatable;
    }

    public void quit() {
        this.updateSubscriptionManager.remove(this);
    }

    public void update() {
        this.updatable.update();
    }
}
