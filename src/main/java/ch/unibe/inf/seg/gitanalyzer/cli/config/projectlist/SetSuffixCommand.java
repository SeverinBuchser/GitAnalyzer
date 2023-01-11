package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "set-suffix",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetSuffixCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Parameters(
            paramLabel = "<suffix>",
            description = "",
            index = "2"

    )
    public void setSuffix(String suffix) {
        if (this.mixin.getConfig().hasLoadException()) return;
        if (this.mixin.hasLoadException()) return;
        this.mixin.getProjectList().setSuffix(suffix);
    }

    @Override
    public void run() {
        this.logger.info("Executing Set Suffix Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        this.logger.info(String.format(
                "Setting Suffix to Project List '%s' of Config '%s'",
                this.mixin.getProjectList().getListPath(),
                this.mixin.getConfig().getConfigPath()
        ));
        try {
            this.mixin.getConfig().save();
            this.logger.success(String.format(
                    "Set Suffix '%s' to Project List '%s' of Config '%s'",
                    this.mixin.getProjectList().getSuffix(),
                    this.mixin.getProjectList().getListPath(),
                    this.mixin.getConfig().getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Set Suffix Command Complete.");
    }
}
