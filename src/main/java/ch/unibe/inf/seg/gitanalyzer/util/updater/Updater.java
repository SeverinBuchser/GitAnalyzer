package ch.unibe.inf.seg.gitanalyzer.util.updater;

public interface Updater {
    UpdateSubscription getUpdates(Updatable updatable);
}
