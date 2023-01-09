package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListsCloner;
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
    public ConfigMixin config;

    @Override
    public void run() {
        this.logger.info("Running Clone Command");
        this.logger.info("Cloning Config: " + this.config.getConfigPath());
        if (this.config.hasLoadException()) {
            this.logger.fail(String.format("Config at '%s' could not be loaded!", this.config.getConfigPathAbsolute()));
            return;
        }
        ProjectListsCloner cloner = new ProjectListsCloner(this.logger);
        cloner.call(this.config.getProjectLists());
        this.logger.success("Cloned Config: " + this.config.getConfigPath());
    }
}
