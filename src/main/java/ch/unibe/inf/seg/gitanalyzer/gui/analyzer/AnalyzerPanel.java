package ch.unibe.inf.seg.gitanalyzer.gui.analyzer;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.clone.Cloner;
import ch.unibe.inf.seg.gitanalyzer.config.Config;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GuiLogger;
import ch.unibe.inf.seg.gitanalyzer.util.subscription.Subscriber;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnalyzerPanel extends JPanel implements Subscriber<Config> {
    private final GuiLogger guiLogger;
    private Config config;

    public AnalyzerPanel() {
        JButton runConfigButton = new JButton("Run Config");
        runConfigButton.addActionListener(a -> this.runConfig());
        this.guiLogger = new GuiLogger();


        this.add(runConfigButton);
        this.add(guiLogger);
    }

    private void runConfig() {
        if (this.config == null) {
            ((JTabbedPane) this.getParent()).setSelectedIndex(0);
        } else {
            try {
                Cloner cloner = new Cloner().addProjectLists(this.config.getProjectLists());
                cloner.call();
                // TODO logger
            } catch (IOException e) {
                // TODO: logger
            }
            ProjectListAnalyzer analyzer = new ProjectListAnalyzer(this.guiLogger);

            for (ProjectList projectList : config.getProjectLists()) {
                try {
                    ProjectListReport report = analyzer.analyze(projectList);

                    File outFile = this.config.getOutPathAbsolute().resolve(projectList.getOutFilename()).toFile();
                    FileWriter writer = new FileWriter(outFile);
                    writer.write(report.report().toString(4));
                    writer.close();
                    System.out.println(outFile);
                } catch (IOException e) {
                    // TODO: logger
                }
            }
            // TODO: logger
        }
    }

    @Override
    public void next(Config config) {
        this.config = config;
    }
}
