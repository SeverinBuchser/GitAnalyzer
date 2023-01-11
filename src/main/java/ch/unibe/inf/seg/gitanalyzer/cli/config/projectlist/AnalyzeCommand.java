package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@CommandLine.Command(
        name = "analyze",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AnalyzeCommand extends AbstractAnalyzeCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    protected File getOutFile(ProjectList projectList) {
        Path out = this.mixin.getConfig().getOutPathAbsolute();
        return out.resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Executing Analyze Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;

        this.analyzeProjectList(this.mixin.getProjectList());
        this.logger.success("Analyze Command Complete.");
    }
}
