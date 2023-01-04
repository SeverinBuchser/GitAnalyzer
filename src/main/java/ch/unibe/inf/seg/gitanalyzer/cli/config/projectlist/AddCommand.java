package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.VersionProvider;
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
    public ProjectListMixin mixin;

    @CommandLine.Option(
            names = {"-d", "--dir"},
            description = "",
            defaultValue = "."
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
        if (!this.mixin.hasNotFoundException()) {
            // TODO: logger already exists
            return;
        }
        try {
            this.mixin.getConfig().getProjectLists().add(this.mixin.getProjectList());
            this.mixin.getConfig().save();
            // TODO: logger
        } catch (IOException e) {
            // TODO: Logger log error
        }
    }
}
