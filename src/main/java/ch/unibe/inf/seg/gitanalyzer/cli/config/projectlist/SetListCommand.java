package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-list",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetListCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Parameters(
            paramLabel = "<list>",
            description = "",
            index = "2"

    )
    public void setList(String list) {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        this.mixin.getProjectList().setList(list);
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
