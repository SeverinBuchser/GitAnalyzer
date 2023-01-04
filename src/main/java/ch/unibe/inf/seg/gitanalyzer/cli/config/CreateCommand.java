package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
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
            defaultValue = "."
    )
    public void setOut(String out) {
        this.config.setOut(out);
    }


    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "",
            defaultValue = "false"
    )
    public void setVerbose(boolean verbose) {
        this.config.setVerbose(verbose);
    }

    @Override
    public void run() {
        if (!this.config.hasLoadException()) {
            // TODO: logger already exists
            return;
        }
        try {
            this.config.save();
            // TODO: logger
        } catch (IOException e) {
            // TODO: Logger log error
        }
    }
}
