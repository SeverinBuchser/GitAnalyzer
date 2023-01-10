package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(
        name = "analyze",
        description = "Runs an analysis of a list of projects.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AnalyzeCommand extends AbstractAnalyzeCommand {

    @CommandLine.Mixin
    public GlobalLogger logger;

    private final ProjectList projectList = new ProjectList("");

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

    @CommandLine.Parameters(
            description = "The list of projects to analyze."
    )
    public void setList(String list) {
        this.projectList.setList(list);
    }

    @Override
    protected File getOutFile(ProjectList projectList) {
        return this.out.resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Running Analyze Command");
        this.logger.info(String.format("Analyzing Project list '%s'", this.projectList.getListPath()));
        this.analyzeProjectList(this.projectList);
    }
}
