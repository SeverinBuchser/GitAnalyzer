package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-analyze",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetAnalyzeCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<analyze>",
            description = "",
            defaultValue = "true",
            index = "1"

    )
    public void setAnalyze(boolean analyze) {
        if (this.config.hasLoadException()) return;
        this.config.setAnalyze(analyze);
    }

    @Override
    public void run() {
        this.logger.info("Executing Set Analyze Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(String.format("Setting Analyze to Config %s.", this.config.getConfigPath()));
        try {
            this.config.save();
            this.logger.success(String.format(
                    "Set Analyze %s to Config %s.",
                    this.config.getAnalyze(),
                    this.config.getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Set Analyze Command Complete.");
    }
}