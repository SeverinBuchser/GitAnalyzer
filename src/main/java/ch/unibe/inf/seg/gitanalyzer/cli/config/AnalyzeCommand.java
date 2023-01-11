package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        name = "analyze",
        description = "Analyzes the project lists of the config, disregarding the analyze property of the config.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AnalyzeCommand extends AbstractAnalyzeCommand {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    protected File getOutFile(ProjectList projectList) {
        return this.config.getOutPathAbsolute().resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Executing Analyze Command...");
        this.logger.info(String.format("Analyzing Config %s", this.config.getConfigPath()));
        if (CommandHelper.configLoadFailed(this.config)) return;

        for (ProjectList projectList : config.getProjectLists()) {
            this.analyzeProjectList(projectList);
        }
        this.logger.success(String.format("Analyzed Config %s", this.config.getConfigPath()));
        this.logger.success("Analyze Command Complete.");
    }
}
