package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.analyze.AsyncAnalyzerWrapper;
import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListsCloner;
import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GuiLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.PrintStreamLogger;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AnalyzerPanel extends JPanel implements Subscriber<Config> {
    private final JPanel loggerPanel = new JPanel();
    private final JSpinner maxThreadCounter = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private int maxThreads = 1;
    private int runningAnalyses = 0;
    private Config config;
    private int currentProjectListIndex = -1;

    public AnalyzerPanel() {
        JButton runConfigButton = new JButton("Run Config");
        runConfigButton.addActionListener(a -> {
            try {
                this.runConfig();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        this.add(runConfigButton);
        this.add(this.loggerPanel);
        this.add(maxThreadCounter);
    }

    private void runConfig() throws InterruptedException {
        if (this.currentProjectListIndex >= 0) return;
        if (this.config == null) {
            ((JTabbedPane) this.getParent()).setSelectedIndex(0);
        } else {
            this.currentProjectListIndex = -1;
            this.maxThreads = (int) this.maxThreadCounter.getValue();
            this.loggerPanel.removeAll();
            this.config.getProjectLists().forEach(pl -> this.loggerPanel.add(new GuiLogger()));
            ProjectListsCloner cloner = new ProjectListsCloner(new PrintStreamLogger(System.out));
            cloner.call(this.config.getProjectLists());
            this.analyzeNextProjectList();
        }
    }

    private void analyzeNextProjectList() {
        this.currentProjectListIndex++;
        if (this.currentProjectListIndex < this.config.getProjectLists().size()) {
            ProjectList projectList = this.config.getProjectLists().get(this.currentProjectListIndex);
            GuiLogger logger = (GuiLogger) this.loggerPanel.getComponent(this.currentProjectListIndex);
            this.runningAnalyses++;
            this.analyzeProjectList(projectList, logger);
        } else {
            this.currentProjectListIndex = -1;
        }
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
            this.runningAnalyses--;
            analyzeNextProjectList();
        });


        asyncAnalyzer.call(projectList);
        if (this.runningAnalyses < this.maxThreads) this.analyzeNextProjectList();
    }

    @Override
    public void next(Config config) {
        this.config = config;
    }
}
