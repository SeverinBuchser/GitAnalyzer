package ch.unibe.inf.seg.gitanalyzer.util.updater;

import java.util.ArrayList;
import java.util.List;

public class UpdateSubscriptionManager {
    private final List<UpdateSubscription> updateSubscriptions = new ArrayList<>();

    public void add(UpdateSubscription updateSubscription) {
        this.updateSubscriptions.add(updateSubscription);
    }

    public void remove(UpdateSubscription updateSubscription) {
        this.updateSubscriptions.remove(updateSubscription);
    }

    public void updateAll() {
        this.updateSubscriptions.forEach(UpdateSubscription::update);
    }

    public UpdateSubscription create(Updatable updatable) {
        UpdateSubscription updateSubscription = new UpdateSubscription(this, updatable);
        this.add(updateSubscription);
        return updateSubscription;
    }
}
