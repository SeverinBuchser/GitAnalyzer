package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
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
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<analyze>",
            description = "",
            defaultValue = "true",
            index = "1"

    )
    public void setAnalyze(boolean analyze) {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        this.config.setAnalyze(analyze);
    }

    @Override
    public void run() {
        try {
            this.config.save();
            // TODO: logger
        } catch (IOException e) {
            // TODO: logger
        }
    }
}