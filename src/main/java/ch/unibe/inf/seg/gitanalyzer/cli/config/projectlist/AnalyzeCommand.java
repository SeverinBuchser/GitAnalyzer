package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@CommandLine.Command(
        name = "analyze",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AnalyzeCommand implements Runnable {

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
