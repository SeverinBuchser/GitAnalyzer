package ch.unibe.inf.seg.gitanalyzer.cli.config;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import picocli.CommandLine;

@CommandLine.Command(
        name = "show",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class ShowCommand implements Runnable {

    @CommandLine.Mixin
    public ConfigMixin config;

    @Override
    public void run() {
        if (this.config.hasLoadException()) {
            // TODO: logger
            return;
        }
        System.out.println(this.config);
        // TODO: logger log config
    }
}
