package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@CommandLine.Command(
        name = "analyze",
        description = "Runs an analysis of a list of projects.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AnalyzeCommand implements Runnable {

    private final ProjectList projectList = new ProjectList();

    @CommandLine.Option(
            names = {"-d", "--dir"},
            description = "The location of the projects in the list of projects <list>.",
            defaultValue = ""
    )
    public void setDir(String dir) {
        this.projectList.setDir(dir);
    }

    @CommandLine.Option(
            names = {"-s", "--suffix"},
            description = "The suffix, appended to the output file.",
            defaultValue = ""
    )
    public void setSuffix(String suffix) {
        this.projectList.setSuffix(suffix);
    }

    private Path out;
    @CommandLine.Option(
            names = {"-o", "--out"},
            description = "The directory of the output file.",
            defaultValue = ""
    )
    public void setOut(String out) {
        this.out = FileHelper.toAbsolutePath(out);
    }

    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "Verbose mode. Additional helpful information will be displayed.",
            defaultValue = "false"
    )
    public void setVerbose(boolean verbose) {

    }

    @CommandLine.Parameters(
            description = "The list of projects to analyze."
    )
    public void setList(String list) {
        this.projectList.setList(list);
    }

    @Override
    public void run() {
        try {
            ProjectListAnalyzer analyzer = new ProjectListAnalyzer();
            ProjectListReport report = analyzer.analyze(this.projectList);

            File outFile = this.out.resolve(Path.of(this.projectList.getOutFilename())).toFile();
            FileWriter writer = new FileWriter(outFile);
            writer.write(report.report().toString(4));
            writer.close();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
