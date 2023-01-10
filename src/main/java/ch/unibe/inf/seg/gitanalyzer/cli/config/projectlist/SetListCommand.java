package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.GlobalLogger;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;

@CommandLine.Command(
        name = "set-list",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class SetListCommand implements Runnable {

    @CommandLine.Mixin
    public GlobalLogger logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    private Path oldListPath;

    @CommandLine.Parameters(
            paramLabel = "<list>",
            description = "",
            index = "2"

    )
    public void setList(String list) {
        if (this.mixin.getConfig().hasLoadException()) return;
        if (this.mixin.hasLoadException()) return;
        this.oldListPath = this.mixin.getProjectList().getListPath();
        this.mixin.getProjectList().setList(list);
    }

    @Override
    public void run() {
        this.logger.info("Running Set List Command");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (CommandHelper.projectListLoadFailed(this.mixin)) return;
        this.logger.info(String.format(
                "Setting List to Project List '%s' of Config '%s'",
                this.mixin.getProjectList().getListPath(),
                this.mixin.getConfig().getConfigPath()
        ));
        try {
            this.mixin.getConfig().save();
            this.logger.success(String.format(
                    "Set List '%s' to Project List '%s' of Config '%s'",
                    this.oldListPath,
                    this.mixin.getProjectList().getListPath(),
                    this.mixin.getConfig().getConfigPath()
            ));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
    }
}
