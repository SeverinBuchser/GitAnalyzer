package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-suffix",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetSuffixCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Parameters(
            paramLabel = "<suffix>",
            description = "",
            index = "2"

    )
    public void setSuffix(String suffix) {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        this.mixin.getProjectList().setSuffix(suffix);
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
