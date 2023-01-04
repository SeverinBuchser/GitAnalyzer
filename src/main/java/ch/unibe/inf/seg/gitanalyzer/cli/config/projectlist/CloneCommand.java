package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.Cloner;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "clone",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand implements Runnable {

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        if (this.mixin.hasNotFoundException()) {
            // TODO: logger
            return;
        }
        try {
            Cloner cloner = new Cloner(this.mixin.getProjectList());
            cloner.call();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
