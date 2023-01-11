package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "create",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class CreateCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Option(
            names = {"-a", "--analyze"},
            description = "",
            defaultValue = "true"
    )
    public void setAnalyze(boolean analyze) {
        this.config.setAnalyze(analyze);
    }

    @CommandLine.Option(
            names = {"-c", "--clone"},
            description = "",
            defaultValue = "true"
    )
    public void setClone(boolean clone) {
        this.config.setClone(clone);
    }


    @CommandLine.Option(
            names = {"-o", "--out"},
            description = "",
            defaultValue = ""
    )
    public void setOut(String out) {
        this.config.setOut(out);
    }

    @Override
    public void run() {
        this.logger.info("Executing Create Command...");
        if (!this.config.hasLoadException()) {
            this.logger.fail(String.format(
                    "Config '%s' already exists.",
                    this.config.getConfigPath()
            ));
            return;
        }
        this.logger.info(String.format("Creating Config '%s'.", this.config.getConfigPath()));
        try {
            this.config.save();
            this.logger.success(String.format("Created Config '%s'.", this.config.getConfigPath()));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Create Command Complete.");
    }
}
