package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;

public class AsyncAnalyzerWrapper<T, R> extends Thread implements Analyzer<T, Void>, Subscribable<R> {

    private final SubscriptionManager<R> subscriptionManager = new SubscriptionManager<>();
    private final Analyzer<T, R> analyzer;
    private T toAnalyze;

    public AsyncAnalyzerWrapper(Analyzer<T, R> analyzer) {
        this.analyzer = analyzer;
    }

    public AsyncAnalyzerWrapper(Analyzer<T, R> analyzer, T toAnalyze) {
        this.analyzer = analyzer;
        this.toAnalyze = toAnalyze;
    }

    @Override
    public Void call(T toAnalyze) {
        this.toAnalyze = toAnalyze;
        this.start();
        return null;
    }

    @Override
    public void run() {
        if (this.toAnalyze == null) throw new IllegalStateException("The object to analyze has not been set.");
        R analysisResult = this.analyzer.call(this.toAnalyze);
        this.subscriptionManager.nextAll(analysisResult);
    }

    @Override
    public Subscription<R> subscribe(Subscriber<R> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
