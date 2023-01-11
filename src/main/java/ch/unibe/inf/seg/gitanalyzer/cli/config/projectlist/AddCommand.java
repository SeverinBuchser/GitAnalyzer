package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.CommandHelper;
import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "add",
        description = "",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class
)
public class AddCommand implements Runnable {

    @CommandLine.Mixin
    public LoggerProvider logger;

    @CommandLine.Mixin
    public ProjectListMixin mixin;

    @CommandLine.Option(
            names = {"-d", "--dir"},
            description = "",
            defaultValue = ""
    )
    public void setDir(String dir) {
        this.mixin.getProjectList().setDir(dir);
    }


    @CommandLine.Option(
            names = {"-sx", "--suffix"},
            description = "",
            defaultValue = ""
    )
    public void setSuffix(String suffix) {
        this.mixin.getProjectList().setSuffix(suffix);
    }


    @CommandLine.Option(
            names = {"-s", "--skip"},
            description = "",
            defaultValue = "false"
    )
    public void setSkip(boolean skip) {
        this.mixin.getProjectList().setSkip(skip);
    }

    @Override
    public void run() {
        this.logger.info("Executing Add Command...");
        if (CommandHelper.configLoadFailed(this.mixin.getConfig())) return;
        if (!this.mixin.hasLoadException()) {
            this.logger.fail(String.format(
                    "Project List '%s' already exists.",
                    this.mixin.getProjectList().getListPath()
            ));
            return;
        }

        try {
            this.logger.info(String.format("Adding Project List '%s'.", this.mixin.getProjectList().getListPath()));
            this.mixin.getConfig().getProjectLists().add(this.mixin.getProjectList());
            this.mixin.getConfig().save();
            this.logger.success(String.format("Added Project List '%s'.", this.mixin.getProjectList().getListPath()));
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
        this.logger.success("Add Command Complete.");
    }
}
