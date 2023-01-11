package ch.unibe.inf.seg.gitanalyzer.gui.config;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.gui.config.editor.ConfigEditor;
import ch.unibe.inf.seg.gitanalyzer.gui.config.viewer.ConfigViewerFrame;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfigPanel extends JPanel implements Updatable, Subscribable<Config>, Subscriber<Config> {
    private final SubscriptionManager<Config> subscriptionManager = new SubscriptionManager<>();
    private Config config;
    private final ConfigEditor configEditor = new ConfigEditor();
    private boolean rawOpen = false;

    public ConfigPanel() {
        ConfigControlPanel controlPanel = new ConfigControlPanel(this);
        controlPanel.subscribe(this);
        this.subscribe(controlPanel);

        controlPanel.addActionListener("raw", a -> this.openRawConfig());
        this.subscribe(this.configEditor);

        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.PAGE_START);
        this.add(this.configEditor, BorderLayout.CENTER);
    }

    private void openRawConfig() {
        if (this.rawOpen) return;
        this.rawOpen = true;
        ConfigViewerFrame configViewerFrame = new ConfigViewerFrame((JFrame) SwingUtilities.getRoot(this));
        Subscription<Config> configSub = this.subscribe(configViewerFrame);
        UpdateSubscription updateSub = this.configEditor.getUpdates(configViewerFrame);
        configViewerFrame.next(this.config);

        configViewerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                configSub.quit();
                updateSub.quit();
                rawOpen = false;
            }
        });
    }

    @Override
    public void next(Config config) {
        this.config = config;
        this.subscriptionManager.nextAll(config);
    }

    @Override
    public void update() {
        this.configEditor.update();
    }

    @Override
    public Subscription<Config> subscribe(Subscriber<Config> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
