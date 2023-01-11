package ch.unibe.inf.seg.gitanalyzer.gui.config.viewer;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfigViewerFrame extends JFrame implements Subscriber<Config>, Updatable {

    private final ConfigViewer configViewer = new ConfigViewer();

    private final WindowAdapter rootListener;

    public ConfigViewerFrame(JFrame root) {
        this.rootListener = this.getRootWindowListener();
        root.addWindowListener(this.rootListener);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(root);


        Dimension minSize = new Dimension(600, 500);
        this.setSize(minSize);
        this.setMinimumSize(minSize);
        this.setLocation(root.getX() + root.getWidth(), root.getY());
        this.add(this.configViewer);
        this.setTitle("Raw Viewer");
        this.setVisible(true);
    }

    private WindowAdapter getRootWindowListener() {
        WindowEvent event = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dispatchEvent(event);
            }
        };
    }

    private void addWindowListener(JFrame root) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                root.removeWindowListener(rootListener);
            }
        });
    }

    @Override
    public void next(Config config) {
        this.configViewer.next(config);
    }

    @Override
    public void update() {
        this.configViewer.update();
    }
}
