package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListsCloner;
import picocli.CommandLine;

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
        ProjectListsCloner cloner = new ProjectListsCloner();
        cloner.call(this.config.getProjectLists());
    }
}
