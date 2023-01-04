package ch.unibe.inf.seg.gitanalyzer.cli.config;

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
    public ConfigMixin config;

    @Override
    public void run() {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        try {
            Cloner cloner = new Cloner().addProjectLists(this.config.getProjectLists());
            cloner.call();
            // TODO logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
