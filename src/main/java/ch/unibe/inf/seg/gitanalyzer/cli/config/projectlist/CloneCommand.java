package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractCloneCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "clone",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand extends AbstractCloneCommand {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        this.logger.info("Executing Clone Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;

        this.cloneProjectList(this.mixin.getProjectList());
        this.logger.success("Clone Command Complete.");
    }
}
