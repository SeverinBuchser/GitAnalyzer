package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-verbose",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetVerboseCommand implements Runnable {

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<verbose>",
            description = "",
            defaultValue = "false",
            index = "1"

    )
    public void setVerbose(boolean verbose) {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        this.config.setVerbose(verbose);
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
