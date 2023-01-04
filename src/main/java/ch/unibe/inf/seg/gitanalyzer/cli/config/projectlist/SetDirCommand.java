package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-dir",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetDirCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Parameters(
            paramLabel = "<dir>",
            description = "",
            index = "2"

    )
    public void setDir(String dir) {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        this.mixin.getProjectList().setDir(dir);
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
