package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
        name = "set-out",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetOutCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<out>",
            description = "",
            defaultValue = "",
            index = "1"

    )
    public void setOut(File out) {
        if (this.config.hasLoadException()) return;
        this.config.setOut(out.toPath());
    }

    @Override
    public void run() {
        this.logger.info("Executing Set Out Command...");
        if (CommandHelper.configLoadFailed(this.config)) return;
        this.logger.info(String.format("Setting Out to Config %s.", this.config.getConfigPath()));
        try {
            this.config.save();
            this.logger.success(String.format(
                    "Set Out %s to Config %s.",
                    this.config.getOutPath(),
                    this.config.getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Set Out Command Complete.");
    }
}
