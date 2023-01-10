package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;

import java.io.IOException;
import java.io.OutputStream;

public class StringBuilderOutputStream extends OutputStream implements Subscribable<String> {

    private final SubscriptionManager<String> subscriptionManager = new SubscriptionManager<>();
    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
        String character = Character.toString((char) b);
        buffer.append(character);
        if (character.equals("\n")) {
            this.subscriptionManager.nextAll(buffer.toString());
        }
    }

    @Override
    public Subscription<String> subscribe(Subscriber<String> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
