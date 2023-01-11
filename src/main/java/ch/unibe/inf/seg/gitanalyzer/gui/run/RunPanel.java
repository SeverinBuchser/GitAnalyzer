package ch.unibe.inf.seg.gitanalyzer.gui.run;

import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RunPanel extends JPanel implements Subscriber<Config> {

    private final JScrollPane loggerScrollPane = new JScrollPane();
    private final JPanel loggerPanel = new JPanel();
    private final RunControlPanel runControlPanel;
    private int maxRunningCount = 1;
    private Config config;

    private ArrayList<ProjectListRunPanel> panels;

    public RunPanel() {

        this.runControlPanel = new RunControlPanel();
        this.runControlPanel.addActionListener("run", a -> this.runConfig());

        this.setLayout(new BorderLayout());
        this.add(this.runControlPanel, BorderLayout.PAGE_START);
        this.loggerPanel.setLayout(new BoxLayout(this.loggerPanel, BoxLayout.Y_AXIS));
        this.loggerScrollPane.add(this.loggerPanel);
        this.loggerScrollPane.setViewportView(this.loggerPanel);
        this.add(this.loggerScrollPane);
    }

    private ProjectListRunPanel getNextAnalysis() {
        for (ProjectListRunPanel panel: this.panels) {
            if (panel.isIdol()) return panel;
        }
        return null;
    }

    private boolean isDone() {
        return this.panels == null || this.panels.stream().allMatch(ProjectListRunPanel::isDone);
    }

    private boolean isMaxRunningAnalyses() {
        return this.panels.stream().filter(ProjectListRunPanel::isRunning).count() == this.maxRunningCount;
    }

    private void runConfig() {
        if (this.config == null) {
            ((JTabbedPane) this.getParent()).setSelectedIndex(0);
        } else if (this.panels == null) {
            LoggerProvider.getLogger().info("Executing Run Command...");
            LoggerProvider.getLogger().info(String.format("Running Config %s.", this.config.getConfigPath()));
            LoggerProvider.getLogger().separator();

            this.loggerPanel.removeAll();
            this.panels = new ArrayList<>();
            this.maxRunningCount = this.runControlPanel.getMaxRunningCount();
            int verbosityLevel = this.runControlPanel.getVerbosityLevel();
            boolean autoSave = this.runControlPanel.getAutoSave();

            for (ProjectList projectList: this.config.getProjectLists()) {
                ProjectListRunPanel panel = new ProjectListRunPanel(projectList, verbosityLevel, autoSave);
                panel.getUpdates(this::analysisDone);
                this.loggerPanel.add(panel);
                this.panels.add(panel);
            }
            this.loggerScrollPane.updateUI();
            this.nextAnalysis();
        }
    }

    private void nextAnalysis() {
        if (!this.isDone() && !this.isMaxRunningAnalyses()) {
            ProjectListRunPanel panel = this.getNextAnalysis();
            if (panel != null) {
                panel.run();
                this.nextAnalysis();
            }
        } else if (this.isDone()) {
            this.panels = null;
            LoggerProvider.getLogger().separator();
            LoggerProvider.getLogger().success("Run Command Complete.");
        }
    }

    private void analysisDone() {
        this.nextAnalysis();
    }

    @Override
    public void next(Config config) {
        this.config = config;
        this.runControlPanel.setMaxRunningCount(config.getProjectLists().size());
        this.runControlPanel.setVerbosityLevel(LoggerProvider.getLogger().getVerbosityLevel());
    }
}
