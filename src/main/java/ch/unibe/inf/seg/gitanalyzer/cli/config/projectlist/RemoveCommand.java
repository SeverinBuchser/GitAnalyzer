package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
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
    public ProjectListMixin mixin;

    @Override
    public void run() {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        try {
            this.mixin.getConfig().getProjectLists().remove(this.mixin.getProjectList());
            this.mixin.getConfig().save();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
