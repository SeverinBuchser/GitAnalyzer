package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

@CommandLine.Command(
        name = "show",
        description = "Prints the config.",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ShowCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    public void run() {
        this.logger.info("Executing Show Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(this.config.toString());
        this.logger.success("Show Command Complete.");
    }
}
