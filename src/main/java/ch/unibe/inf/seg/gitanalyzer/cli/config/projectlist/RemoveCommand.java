package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "remove",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class RemoveCommand implements Runnable {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        this.logger.info("Running Remove Project List Command");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        this.logger.info(String.format(
                "Removing Project List '%s' from Config '%s'",
                this.mixin.getProjectList().getListPath(),
                this.mixin.getConfig().getConfigPath()
        ));
        try {
            this.mixin.getConfig().getProjectLists().remove(this.mixin.getProjectList());
            this.mixin.getConfig().save();
            this.logger.success(String.format(
                    "Removed Project List '%s' from Config '%s'",
                    this.mixin.getProjectList().getListPath(),
                    this.mixin.getConfig().getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
    }
}
