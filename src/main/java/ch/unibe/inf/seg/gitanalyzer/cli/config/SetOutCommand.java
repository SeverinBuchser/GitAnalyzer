package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-out",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetOutCommand implements Runnable {

    @CommandLine.Mixin
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<out>",
            description = "",
            defaultValue = "",
            index = "1"

    )
    public void setOut(String out) {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        this.config.setOut(out);
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
