package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
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
    public ConfigMixin config;

    @Override
    public void run() {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }

        ProjectListAnalyzer analyzer = new ProjectListAnalyzer();

        for (ProjectList projectList : config.getProjectLists()) {
            try {
                ProjectListReport report = analyzer.analyze(projectList);

                File outFile = this.config.getOutRelative().resolve(Path.of(projectList.getOutFilename())).toFile();
                FileWriter writer = new FileWriter(outFile);
                writer.write(report.report().toString(4));
                writer.close();
            } catch (IOException e) {

            }
        }
    }
}
