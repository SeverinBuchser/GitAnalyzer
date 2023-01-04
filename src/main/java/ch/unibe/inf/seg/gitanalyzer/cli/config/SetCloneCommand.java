package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
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
    public ConfigMixin config;

    @CommandLine.Parameters(
            paramLabel = "<clone>",
            description = "",
            defaultValue = "true",
            index = "1"

    )
    public void setClone(boolean clone) {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        this.config.setClone(clone);
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
