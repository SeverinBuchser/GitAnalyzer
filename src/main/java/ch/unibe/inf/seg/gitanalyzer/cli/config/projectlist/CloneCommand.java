package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.util.logger.CliLogger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "clone",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand implements Runnable {

    @CommandLine.Mixin
    public CliLogger logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @Override
    public void run() {
        this.logger.info("Running Clone Command");
        if (this.mixin.getConfig().hasLoadException()) {
            this.logger.fail(String.format("Config at '%s' could not be loaded!",
                    this.mixin.getConfig().getConfigPathAbsolute()));
            return;
        }
        if (this.mixin.hasNotFoundException()) {
            this.logger.fail(String.format("Project List at '%s' could not be loaded!",
                    this.mixin.getProjectList().getListPath()));
            return;
        }
        ProjectListCloner cloner = new ProjectListCloner(logger);
        cloner.call(this.mixin.getProjectList());
    }
}
