package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

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
    public ProjectListMixin mixin;

    @Override
    protected File getOutFile(ProjectList projectList) {
        Path out = this.mixin.getConfig().getOutPathAbsolute();
        return out.resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Executing Run Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        this.logger.info(String.format("Running Project List '%s'.", this.mixin.getProjectList().getListPath()));
        
        if (this.mixin.getConfig().getClone()) {
            this.logger.info("Cloning...");
            ProjectListCloner cloner = new ProjectListCloner(LoggerProvider.getLogger());
            cloner.call(this.mixin.getProjectList());
            this.logger.success("Cloning Complete.");
        }
        if (this.mixin.getConfig().getAnalyze()) {
            this.logger.info("Analyzing...");
            this.analyzeProjectList(this.mixin.getProjectList());
            this.logger.success("Analysis Complete.");
        }
        this.logger.success("Run Command Complete.");
    }
}