package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
        name = "run",
        description = "Runs the config. If the clone property is set to true in the config, all project lists will be" +
                " cloned. If the analyze property is set to true in the config, all project lists will be analyzed." +
                " The cloning will happen first.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class RunCommand extends AbstractAnalyzeCommand {

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
        this.logger.info("Executing Run Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(String.format("Running Config %s.", this.config.getConfigPath()));
        if (this.config.getClone() || this.config.getAnalyze()) {
            this.logger.separator();
        }

        if (this.config.getClone()) {
            this.logger.info("Cloning...");
            ProjectListCloner cloner = new ProjectListCloner(this.logger);
            for (ProjectList projectList: this.config.getProjectLists()) {
                if (projectList.getSkip()) {
                    this.logger.info(String.format(
                            "Project List %s skipped.",
                            projectList.getListPath()
                    ));
                } else {
                    this.logger.info(String.format("Cloning Project List %s.", projectList.getListPath()));
                    this.logger.separator(1);
                    cloner.call(projectList);
                    this.logger.separator(1);
                    this.logger.success(String.format("Cloned Project List %s.", projectList.getListPath()));
                }
            }
            this.logger.success("Cloning Complete.");
            this.logger.separator();
        }

        if (this.config.getAnalyze()) {
            this.logger.info("Analyzing...");
            for (ProjectList projectList : config.getProjectLists()) {
                this.analyzeProjectList(projectList);
            }
            this.logger.success("Analysis Complete.");
            this.logger.separator();
        }
        this.logger.success("Run Command Complete.");
    }
}