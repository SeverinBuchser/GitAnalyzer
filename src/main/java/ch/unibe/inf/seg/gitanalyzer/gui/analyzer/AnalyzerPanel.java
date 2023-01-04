package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;

public class AnalyzerPanel extends JPanel implements Subscriber<Config> {
    private Config config;

    public AnalyzerPanel() {
        JButton runConfigButton = new JButton("Run Config");
        runConfigButton.addActionListener(a -> this.runConfig());
        this.add(runConfigButton);
    }

    private void runConfig() {
        if (this.config == null) {
            ((JTabbedPane) this.getParent()).setSelectedIndex(0);
        }
    }

    @Override
    public void next(Config config) {
        this.config = config;
    }
}
