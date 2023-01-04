package ch.unibe.inf.seg.gitanalyzer.gui.config;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
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
import java.awt.event.WindowListener;

public class ConfigPanel extends JPanel implements Updatable, Subscribable<Config>, Subscriber<Config> {

    private final SubscriptionManager<Config> subscriptionManager = new SubscriptionManager<>();

    private Config config;
    private final ConfigEditor configEditor;

    private final JButton loadFileButton = new JButton("Load Config");
    private final JButton openRawButton = new JButton("Open Raw Config");
    private final JButton saveFileButton = new JButton("Save Config");
    private final JButton saveAsFileButton = new JButton("Save Config As");
    private final JFileChooser loadFileChooser = new JFileChooser(".");
    private final JFileChooser saveFileChooser = new JFileChooser(".");

    private boolean rawOpen = false;

    public ConfigPanel() {
        this.configEditor = new ConfigEditor();

        JPanel ioPanel = new JPanel();
        ioPanel.add(this.loadFileButton);
        ioPanel.add(this.openRawButton);
        ioPanel.add(this.saveFileButton);
        ioPanel.add(this.saveAsFileButton);

        this.initActionListeners();
        this.initSubscriptions();

        this.setLayout(new BorderLayout());
        this.add(ioPanel, BorderLayout.PAGE_START);
        this.add(this.configEditor, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        this.loadFileButton.addActionListener(a -> this.loadConfig());
        this.openRawButton.addActionListener(a -> this.openRawConfig());
        this.saveFileButton.addActionListener(a -> this.saveConfig());
        this.saveAsFileButton.addActionListener(a -> this.saveConfigAs());
    }

    private void initSubscriptions() {
        this.subscribe(this.configEditor);
    }

    private void loadConfig() {
        JFrame frame = new JFrame();
        int state = this.loadFileChooser.showOpenDialog(frame);
        if (state == JFileChooser.APPROVE_OPTION) {
            String configPath = this.loadFileChooser.getSelectedFile().toPath().toString();
            try {
                Config config = new Config(configPath);
                this.next(config);
            } catch (Exception e) {
                System.err.println("Failed");
            }
        }
        frame.dispose();
    }

    private void openRawConfig() {
        if (this.rawOpen) return;
        this.rawOpen = true;
        ConfigViewer configViewer = new ConfigViewer();
        Subscription<Config> configSub = this.subscribe(configViewer);
        UpdateSubscription updateSub = this.configEditor.getUpdates(configViewer);
        configViewer.next(this.config);

        JFrame frame = new JFrame();
        Dimension minSize = new Dimension(600, 500);
        frame.setSize(minSize);
        frame.setMinimumSize(minSize);

        JFrame root = (JFrame) SwingUtilities.getRoot(this);
        WindowListener rootListener = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        };
        root.addWindowListener(rootListener);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                configSub.quit();
                updateSub.quit();
                root.removeWindowListener(rootListener);
                rawOpen = false;
            }
        });
        frame.setLocation(root.getX() + root.getWidth(), root.getY());
        frame.add(configViewer);
        frame.setVisible(true);
    }

    private void saveConfig() {
        try {
            if (this.config.hasConfigPath()) {
                this.config.save();
            }
        } catch (Exception e) {
            System.err.println("Failed");
        }
    }

    private void saveConfigAs() {
        JFrame frame = new JFrame();
        int state = this.saveFileChooser.showSaveDialog(frame);
        if (state == JFileChooser.APPROVE_OPTION) {
            String configPath = this.saveFileChooser.getSelectedFile().toPath().toString();
            try {
                this.config.setAndSave(configPath);
            } catch (Exception e) {
                System.err.println("Failed");
            }
        }
        frame.dispose();
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
