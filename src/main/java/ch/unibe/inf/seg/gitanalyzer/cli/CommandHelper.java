package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.cli.config.ConfigMixin;
import ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist.ProjectListMixin;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;

public class CommandHelper {
    public static boolean configLoadFailed(ConfigMixin config) {
        if (config.hasLoadException()) {
            LoggerProvider.getLogger().fail(
                    String.format("Config at '%s' could not be loaded!", config.getConfigPathAbsolute())
            );
            return true;
        }
        return false;
    }

    public static boolean projectListLoadFailed(ProjectListMixin mixin) {
        if (mixin.hasLoadException()) {
            LoggerProvider.getLogger().fail(String.format(
                    "Project List at '%s' could not be loaded or was not found!",
                    mixin.getProjectList().getListPath()
            ));
            return true;
        }
        return false;
    }
}
