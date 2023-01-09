package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import picocli.CommandLine;

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
        ProjectListCloner cloner = new ProjectListCloner();
        cloner.call(this.mixin.getProjectList());
    }
}
