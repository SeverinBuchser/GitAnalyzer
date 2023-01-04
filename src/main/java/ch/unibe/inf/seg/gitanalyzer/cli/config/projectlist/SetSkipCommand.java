package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-skip",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetSkipCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Parameters(
            paramLabel = "<skip>",
            description = "",
            index = "2"

    )
    public void setSkip(boolean skip) {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        this.mixin.getProjectList().setSkip(skip);
    }

    @Override
    public void run() {
        try {
            this.mixin.getConfig().save();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
