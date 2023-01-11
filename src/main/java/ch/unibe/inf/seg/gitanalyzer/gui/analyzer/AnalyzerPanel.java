package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.analyze.AsyncAnalyzerWrapper;
import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.clone.AsyncClonerWrapper;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GuiLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AnalyzerPanel extends JPanel implements Subscriber<Config> {
    private final JPanel loggerPanel = new JPanel();
    private final AnalyzerControlPanel analyzerControlPanel;
    private int maxThreads = 1;
    private int runningThreads = 0;
    private Config config;
    private int currentProjectListIndex = -1;

    public AnalyzerPanel() {

        this.analyzerControlPanel = new AnalyzerControlPanel();
        this.analyzerControlPanel.addActionListener("run", a -> this.runConfig());

        this.setLayout(new BorderLayout());
        this.add(this.analyzerControlPanel, BorderLayout.PAGE_START);
        this.add(this.loggerPanel);
    }

    private void runConfig() {
        if (this.currentProjectListIndex >= 0) return;
        if (this.config == null) {
            ((JTabbedPane) this.getParent()).setSelectedIndex(0);
        } else {
            this.loggerPanel.removeAll();
            this.maxThreads = this.analyzerControlPanel.getMaxThreadCount();
            int verbosityLevel = this.analyzerControlPanel.getVerbosityLevel();
            this.config.getProjectLists().forEach(pl -> this.loggerPanel.add(new ProjectListAnalyzerPanel(verbosityLevel)));
            LoggerProvider.getLogger().info(String.format("Running Config '%s'.", this.config.getConfigPath()));
            this.cloneNextProjectList();
        }
    }

    private void cloneNextProjectList() {
        this.currentProjectListIndex++;
        if (this.currentProjectListIndex < this.config.getProjectLists().size()) {
            ProjectList projectList = this.config.getProjectLists().get(this.currentProjectListIndex);
            GuiLogger logger = ((ProjectListAnalyzerPanel) this.loggerPanel.getComponent(this.currentProjectListIndex)).getLogger();
            this.runningThreads++;
            this.cloneProjectList(projectList, logger);
        } else {
            this.currentProjectListIndex = -1;
            this.analyzeNextProjectList();
        }
    }

    private void analyzeNextProjectList() {
        this.currentProjectListIndex++;
        if (this.currentProjectListIndex < this.config.getProjectLists().size()) {
            ProjectList projectList = this.config.getProjectLists().get(this.currentProjectListIndex);
            GuiLogger logger = ((ProjectListAnalyzerPanel) this.loggerPanel.getComponent(this.currentProjectListIndex)).getLogger();
            this.runningThreads++;
            this.analyzeProjectList(projectList, logger);
        } else {
            this.currentProjectListIndex = -1;
        }
    }

    private void cloneProjectList(ProjectList projectList, GuiLogger logger) {
        ProjectListCloner cloner = new ProjectListCloner(logger);
        AsyncClonerWrapper<ProjectList> asyncCloner = new AsyncClonerWrapper<>(cloner);
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    asyncCloner.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        topFrame.addWindowListener(listener);
        asyncCloner.getUpdates(() -> {
            topFrame.removeWindowListener(listener);
            this.runningThreads--;
            this.cloneNextProjectList();
        });


        asyncCloner.call(projectList);
        if (this.runningThreads < this.maxThreads) this.cloneNextProjectList();
    }

    private void analyzeProjectList(ProjectList projectList, GuiLogger logger) {
        ProjectListAnalyzer analyzer = new ProjectListAnalyzer(logger);
        AsyncAnalyzerWrapper<ProjectList, ProjectListReport> asyncAnalyzer =
                new AsyncAnalyzerWrapper<>(analyzer);
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    asyncAnalyzer.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        topFrame.addWindowListener(listener);
        asyncAnalyzer.subscribe(report -> {
            topFrame.removeWindowListener(listener);
            this.runningThreads--;
            this.analyzeNextProjectList();
        });


        asyncAnalyzer.call(projectList);
        if (this.runningThreads < this.maxThreads) this.analyzeNextProjectList();
    }

    @Override
    public void next(Config config) {
        this.config = config;
    }
}
