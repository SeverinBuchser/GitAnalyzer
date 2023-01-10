package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-clone",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetCloneCommand implements Runnable {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<clone>",
            description = "",
            defaultValue = "true",
            index = "1"

    )
    public void setClone(boolean clone) {
        if (this.config.hasLoadException()) return;
        this.config.setClone(clone);
    }

    @Override
    public void run() {
        this.logger.info("Executing Set Clone Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(String.format("Setting Clone to Config '%s'.", this.config.getConfigPath()));
        try {
            this.config.save();
            this.logger.success(String.format(
                    "Set Clone '%s' to Config '%s'.",
                    this.config.getClone(),
                    this.config.getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Set Clone Command Complete.");
    }
}
