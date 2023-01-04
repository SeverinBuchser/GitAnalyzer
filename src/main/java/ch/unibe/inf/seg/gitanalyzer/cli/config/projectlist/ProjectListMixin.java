package ch.unibe.inf.seg.gitanalyzer.cli.config.projectlist;

import ch.unibe.inf.seg.gitanalyzer.cli.config.ConfigMixin;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import picocli.CommandLine;

import java.io.IOException;

public class ProjectListMixin {

    private ProjectListNotFoundException notFoundException;

    public boolean hasNotFoundException() {
        return this.notFoundException != null;
    }

    public ProjectListNotFoundException getNotFoundException() {
        return this.notFoundException;
    }

    @CommandLine.Mixin
    public ConfigMixin mixin;

    private ProjectList projectList;

    @CommandLine.Parameters(
            paramLabel = "<project-list>",
            description = "The name of the project list.",
            index = "1"
    )
    public void setProjectList(String list) {
        if (this.mixin.hasLoadException()) {
            // TODO: logger and exit
        }
        this.projectList = this.mixin.getProjectLists().get(list);
        if (this.projectList == null) {
            this.projectList = new ProjectList(list);
            this.notFoundException = new ProjectListNotFoundException(list);
        }
    }

    public ProjectList getProjectList() {
        return this.projectList;
    }

    public ConfigMixin getConfig() {
        return this.mixin;
    }

    public static class ProjectListNotFoundException extends IOException {
        public ProjectListNotFoundException(String list) {
            super(String.format("List info '%s' was not found in the config.", list));
        }
    }
}
