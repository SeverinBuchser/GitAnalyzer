package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.clone.ProjectListCloner;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;

public abstract class AbstractCloneCommand implements Runnable {
    protected void cloneProjectList(ProjectList projectList) {
        ProjectListCloner cloner = new ProjectListCloner(LoggerProvider.getLogger());
        cloner.call(projectList);
    }
}
