package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

@CommandLine.Command(
        name = "show",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ShowCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        System.out.println(this.mixin.getProjectList());
        // TODO: show list info
    }
}
