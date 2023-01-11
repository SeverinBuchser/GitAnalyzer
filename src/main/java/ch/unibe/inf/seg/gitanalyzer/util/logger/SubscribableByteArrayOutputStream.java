package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SubscribableByteArrayOutputStream extends ByteArrayOutputStream implements Subscribable<String> {
    private final SubscriptionManager<String> subscriptionManager = new SubscriptionManager<>();

    @Override
    public void write(int b) {
        super.write(b);
        String character = Character.toString((char) b);
        if (character.equals("\n")) {
            this.subscriptionManager.nextAll(this.toString());
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        this.subscriptionManager.nextAll(this.toString());
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        super.write(b, off, len);
        this.subscriptionManager.nextAll(this.toString());
    }

    @Override
    public void writeBytes(byte[] b) {
        super.writeBytes(b);
        this.subscriptionManager.nextAll(this.toString());
    }

    @Override
    public Subscription<String> subscribe(Subscriber<String> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
