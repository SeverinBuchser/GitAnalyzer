package ch.unibe.inf.seg.gitanalyzer.gui.run;

import ch.unibe.inf.seg.gitanalyzer.analyze.AsyncAnalyzerWrapper;
import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.clone.AsyncClonerWrapper;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.report.Report;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GuiLogger;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updatable;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscription;
import ch.unibe.inf.seg.gitanalyzer.util.updater.UpdateSubscriptionManager;
import ch.unibe.inf.seg.gitanalyzer.util.updater.Updater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProjectListRunPanel extends JPanel implements Updater, Runnable {
    private final UpdateSubscriptionManager updateSubscriptionManager = new UpdateSubscriptionManager();
    private final GuiLogger logger = new GuiLogger();

    private final JPanel content = new JPanel();
    private final JTextArea textArea = new JTextArea(20, 40);
    private final JScrollPane scrollPane = new JScrollPane();
    private final ProjectList projectList;
    private final boolean autoSave;

    private State state = State.IDOL;
    private Report report;

    public boolean isIdol() {
        return this.state.equals(State.IDOL);
    }

    public boolean isRunning() {
        return this.state.equals(State.RUNNING);
    }

    public boolean isDone() {
        return this.state.equals(State.DONE);
    }

    public GuiLogger getLogger() {
        return logger;
    }

    public ProjectListRunPanel(ProjectList projectList, int verbosityLevel, boolean autoSave) {
        this.projectList = projectList;
        this.logger.setVerbosityLevel(verbosityLevel);
        this.autoSave = autoSave;
        
        this.logger.getOut().subscribe(text -> {
            this.textArea.setText(text);
            JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
        this.textArea.setEditable(false);
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.scrollPane.add(this.textArea);
        this.scrollPane.setViewportView(this.textArea);

        this.content.setLayout(new BorderLayout());
        this.content.add(this.scrollPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        JLabel label = new JLabel(this.projectList.getListPath().toString());
        label.setBorder(new EmptyBorder(new Insets(10, 5, 5, 0)));
        this.add(label, BorderLayout.PAGE_START);
        this.add(content, BorderLayout.CENTER);
    }

    @Override
    public void run() {
        this.state = State.RUNNING;
        if (this.projectList.getSkip()) {
            this.logger.info(String.format(
                    "Project List %s skipped.",
                    projectList.getListPath()
            ));
            this.state = State.DONE;
            this.updateSubscriptionManager.updateAll();
            return;
        }
        this.logger.info(String.format("Running Project List %s.", this.projectList.getListPath()));
        this.logger.separator();
        if (this.projectList.getProjectLists().getConfig().getClone()) {
            this.runClone();
        } else if (this.projectList.getProjectLists().getConfig().getAnalyze()) {
            this.runAnalysis();
        } else {
            this.complete(null);
        }
    }

    private void runClone() {
        this.logger.info("Cloning...");
        this.logger.info(String.format("Cloning Project List %s.", this.projectList.getListPath()));
        this.logger.separator(1);

        ProjectListCloner cloner = new ProjectListCloner(this.logger);
        AsyncClonerWrapper<ProjectList> asyncCloner = new AsyncClonerWrapper<>(cloner);
        asyncCloner.getUpdates(this::runAnalysisOnCloningComplete);
        asyncCloner.call(this.projectList);
    }

    private void runAnalysisOnCloningComplete() {
        this.logger.separator(1);
        this.logger.success(String.format("Cloned Project List %s.", this.projectList.getListPath()));
        this.logger.success("Cloning Complete.");
        this.logger.separator();
        if (this.projectList.getProjectLists().getConfig().getAnalyze()) {
            this.runAnalysis();
        } else {
            this.complete(null);
        }
    }

    private void runAnalysis() {
        this.logger.info("Analyzing...");
        this.logger.info(String.format("Analyzing Project List %s.", this.projectList.getListPath()));
        this.logger.separator(1);

        ProjectListAnalyzer analyzer = new ProjectListAnalyzer(this.logger);
        AsyncAnalyzerWrapper<ProjectList, ProjectListReport> asyncAnalyzer = new AsyncAnalyzerWrapper<>(analyzer);
        asyncAnalyzer.subscribe(report -> {
            this.logger.separator(1);
            this.logger.success(String.format("Analyzed Project List %s.", this.projectList.getListPath()));
            this.logger.success("Analysis Complete.");
            this.logger.separator();
            this.complete(report);
        });
        asyncAnalyzer.call(this.projectList);
    }

    private void complete(Report report) {
        this.report = report;
        if (report != null && !this.autoSave) {
            JButton saveButton = new JButton("Save Report");
            saveButton.addActionListener(a -> this.saveOutFile());
            this.content.add(saveButton, BorderLayout.PAGE_START);
            this.content.updateUI();
        } else if (report != null) {
            this.saveOutFile();
        }
        this.state = State.DONE;
        this.logger.info(String.format("Ran Project List %s.", this.projectList.getListPath()));
        this.updateSubscriptionManager.updateAll();
    }

    private void saveOutFile() {
        try {
            this.logger.info(String.format(
                    "Saving Analysis Report for Project List %s.",
                    this.projectList.getListPath()
            ));
            File outFile = this.projectList.getProjectLists().getConfig().getOutPathAbsolute()
                    .resolve(this.projectList.getOutFilename()).toFile();
            FileWriter writer = new FileWriter(outFile);
            writer.write(this.report.report().toString(4));
            writer.close();
            this.logger.success(String.format("Saved Analysis Report to %s.", outFile));
            this.logger.separator();
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
    }

    @Override
    public UpdateSubscription getUpdates(Updatable updatable) {
        return this.updateSubscriptionManager.create(updatable);
    }

    private enum State {
        IDOL,
        RUNNING,
        DONE
    }
}
