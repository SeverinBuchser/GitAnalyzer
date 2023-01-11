package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

@CommandLine.Command(
        name = "show",
        description = "Prints the project list.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ShowCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        this.logger.info("Executing Show Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        this.logger.info(this.mixin.getProjectList().toString());
        this.logger.success("Show Command Complete.");
    }
}
