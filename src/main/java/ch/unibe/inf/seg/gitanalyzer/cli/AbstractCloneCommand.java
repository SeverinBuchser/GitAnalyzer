package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;

public abstract class AbstractCloneCommand implements Runnable {
    protected void cloneProjectList(ProjectList projectList) {
        LoggerProvider.getLogger().info(String.format("Cloning Project List '%s'.", projectList.getListPath()));
        ProjectListCloner cloner = new ProjectListCloner(LoggerProvider.getLogger());
        cloner.call(projectList);
        LoggerProvider.getLogger().success(String.format("Cloned Project List '%s'.", projectList.getListPath()));
    }
}
