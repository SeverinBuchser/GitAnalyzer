package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.CliLogger;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

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
    public ProjectListMixin mixin;

    private File getOutFile() {
        Path out = this.mixin.getConfig().getOutPathAbsolute();
        return out.resolve(this.mixin.getProjectList().getOutFilename()).toFile();
    }

    @Override
    public void run() {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        
        if (this.mixin.getConfig().getClone()) {
            this.runClone();
        }

        if (this.mixin.getConfig().getAnalyze()) {
            this.runAnalyze();
        }
    }

    private void runClone() {
        ProjectListCloner cloner = new ProjectListCloner(this.logger);
        cloner.call(this.mixin.getProjectList());
    }

    private void runAnalyze() {
        try {
            ProjectListAnalyzer analyzer = new ProjectListAnalyzer();
            ProjectListReport report = analyzer.call(this.mixin.getProjectList());

            File outFile = this.getOutFile();
            FileWriter writer = new FileWriter(outFile);
            writer.write(report.report().toString(4));
            writer.close();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}