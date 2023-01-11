package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "remove",
        description = "Removes a project list from the config.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class RemoveCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        this.logger.info("Executing Remove Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        try {
            this.logger.info(String.format("Removing Project List %s.", this.mixin.getProjectList().getListPath()));
            this.mixin.getConfig().getProjectLists().remove(this.mixin.getProjectList());
            this.mixin.getConfig().save();
            this.logger.success(String.format("Removed Project List %s.", this.mixin.getProjectList().getListPath()));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Remove Command Complete.");
    }
}
