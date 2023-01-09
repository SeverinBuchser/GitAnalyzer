package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectLists;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

public class ProjectListsCloner implements Cloner<ProjectLists> {
    private final ProjectListCloner subCloner;

    public ProjectListsCloner(Logger logger) {
        this.subCloner = new ProjectListCloner(logger);
    }

    @Override
    public void call(ProjectLists projectLists) {
        for (ProjectList projectList: projectLists) {
            subCloner.call(projectList);
        }
    }
}
