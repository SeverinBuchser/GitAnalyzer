package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.AbstractCloneCommand;
import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

@CommandLine.Command(
        name = "clone",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CloneCommand extends AbstractCloneCommand {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    public void run() {
        this.logger.info("Running Clone Command");
        this.logger.info("Cloning Config: " + this.config.getConfigPath());
        if (CommandHelper.configLoadFailed(this.config)) return;

        for (ProjectList projectList: this.config.getProjectLists()) {
            this.cloneProjectList(projectList);
        }
        this.logger.success("Cloned Config: " + this.config.getConfigPath());
    }
}
