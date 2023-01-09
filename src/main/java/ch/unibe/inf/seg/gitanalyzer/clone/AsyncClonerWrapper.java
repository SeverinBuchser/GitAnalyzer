package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updater;

public class AsyncClonerWrapper<T> extends Thread implements Cloner<T>, Updater {
    private final UpdateSubscriptionManager updateSubscriptionManager = new UpdateSubscriptionManager();

    private final Cloner<T> cloner;
    private T toClone;

    public AsyncClonerWrapper(Cloner<T> cloner) {
        this.cloner = cloner;
    }

    public AsyncClonerWrapper(Cloner<T> cloner, T toClone) {
        this.cloner = cloner;
        this.toClone = toClone;
    }

    @Override
    public void call(T toClone) {
        this.toClone = toClone;
        this.start();
    }

    @Override
    public void run() {
        if (this.toClone == null) throw new IllegalStateException("The object to clone has not been set.");
        this.cloner.call(this.toClone);
        this.updateSubscriptionManager.updateAll();
    }

    @Override
    public UpdateSubscription getUpdates(Updatable updatable) {
        return this.updateSubscriptionManager.create(updatable);
    }
}
