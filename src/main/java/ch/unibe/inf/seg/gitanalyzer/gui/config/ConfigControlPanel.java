package ch.unibe.inf.seg.gitanalyzer.gui.config;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.util.gui.ControlPanel;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscribable;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscription;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.SubscriptionManager;

import javax.swing.*;
import java.nio.file.Path;

public class ConfigControlPanel extends ControlPanel implements Subscribable<Config>, Subscriber<Config> {

    private final SubscriptionManager<Config> subscriptionManager = new SubscriptionManager<>();

    private Config config;
    private final ConfigPanel configPanel;

    private final static String CWD = Path.of("").toAbsolutePath().toString();
    private final JFileChooser loadFileChooser = new JFileChooser(CWD);
    private final JFileChooser saveFileChooser = new JFileChooser(CWD);

    public ConfigControlPanel(ConfigPanel configPanel) {
        this.configPanel = configPanel;

        this.addControl("New", "new");
        this.addControl("Load", "load");
        this.addControl("Open Raw", "raw");
        this.addControl("Save", "save");
        this.addControl("Save As", "saveas");
        this.addControl("Run", "run");

        this.addActionListener("new", a -> this.newConfig());
        this.addActionListener("load", a -> this.loadConfig());
        this.addActionListener("save", a -> this.saveConfig());
        this.addActionListener("saveas", a -> this.saveConfigAs());
        this.addActionListener("run", a -> this.runConfig());
    }

    private void newConfig() {
        this.subscriptionManager.nextAll(new Config());
    }

    private void loadConfig() {
        JFrame frame = new JFrame();
        int state = this.loadFileChooser.showOpenDialog(frame);
        if (state == JFileChooser.APPROVE_OPTION) {
            Path configPath = this.loadFileChooser.getSelectedFile().toPath();
            try {
                Config config = new Config(configPath);
                this.subscriptionManager.nextAll(config);
            } catch (Exception e) {
                LoggerProvider.getLogger().fail(e.getMessage());
            }
        }
        frame.dispose();
    }

    private void saveConfig() {
        try {
            if (this.config.hasConfigPath()) {
                this.config.save();
            } else {
                this.saveConfigAs();
            }
        } catch (Exception e) {
            LoggerProvider.getLogger().fail(e.getMessage());
        }
    }

    private void saveConfigAs() {
        JFrame frame = new JFrame();
        int state = this.saveFileChooser.showSaveDialog(frame);
        if (state == JFileChooser.APPROVE_OPTION) {
            Path configPath = this.saveFileChooser.getSelectedFile().toPath();
            try {
                this.config.setAndSave(configPath);
            } catch (Exception e) {
                LoggerProvider.getLogger().fail(e.getMessage());
            }
        }
        frame.dispose();
    }

    private void runConfig() {
        ((JTabbedPane) this.configPanel.getParent()).setSelectedIndex(1);
    }

    @Override
    public void next(Config config) {
        this.config = config;
    }

    @Override
    public Subscription<Config> subscribe(Subscriber<Config> subscriber) {
        return this.subscriptionManager.create(subscriber);
    }
}
