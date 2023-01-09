package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListsCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.CliLogger;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@CommandLine.Command(
        name = "run",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class RunCommand implements Runnable {

    @CommandLine.Mixin
    public CliLogger logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    public void run() {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }

        if (this.config.getClone()) {
            this.runClone();
        }

        if (this.config.getAnalyze()) {
            this.runAnalyze();
        }
    }

    private void runClone() {
        ProjectListsCloner cloner = new ProjectListsCloner(this.logger);
        cloner.call(this.config.getProjectLists());
    }

    private void runAnalyze() {
        ProjectListAnalyzer analyzer = new ProjectListAnalyzer();

        for (ProjectList projectList : config.getProjectLists()) {
            try {
                ProjectListReport report = analyzer.call(projectList);

                File outFile = this.config.getOutPathAbsolute().resolve(projectList.getOutFilename()).toFile();
                FileWriter writer = new FileWriter(outFile);
                writer.write(report.report().toString(4));
                writer.close();
            } catch (IOException e) {
                // TODO: logger
            }
        }
        // TODO: logger
    }
}