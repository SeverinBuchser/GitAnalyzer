package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.cli.AbstractAnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
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
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    protected File getOutFile(ProjectList projectList) {
        Path out = this.mixin.getConfig().getOutPathAbsolute();
        return out.resolve(projectList.getOutFilename()).toFile();
    }

    @Override
    public void run() {
        this.logger.info("Running Analyze Command");
        this.logger.info(String.format(
                "Analyzing Project List '%s' of Config '%s'",
                this.mixin.getProjectList().getListPath(),
                this.mixin.getConfig().getConfigPath()
        ));
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;

        this.analyzeProjectList(this.mixin.getProjectList());
    }
}
