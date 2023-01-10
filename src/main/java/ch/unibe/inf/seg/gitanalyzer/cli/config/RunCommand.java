package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        name = "run",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class RunCommand extends AbstractAnalyzeCommand {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    protected File getOutFile(ProjectList projectList) {
        return this.config.getOutPathAbsolute().resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Executing Run Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(String.format("Running Config '%s'.", this.config.getConfigPath()));

        if (this.config.getClone()) {
            this.logger.info("Cloning...");
            ProjectListCloner cloner = new ProjectListCloner(LoggerProvider.getLogger());
            for (ProjectList projectList: this.config.getProjectLists()) {
                cloner.call(projectList);
            }
            this.logger.success("Cloning Complete.");
        }

        if (this.config.getAnalyze()) {
            this.logger.info("Analyzing...");
            for (ProjectList projectList : config.getProjectLists()) {
                this.analyzeProjectList(projectList);
            }
            this.logger.success("Analysis Complete.");
        }
        this.logger.success("Run Command Complete.");
    }
}