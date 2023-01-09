package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectLists;

public class ProjectListsCloner implements Cloner<ProjectLists> {
    private static final ProjectListCloner subCloner = new ProjectListCloner();
    @Override
    public void call(ProjectLists projectLists) {
        for (ProjectList projectList: projectLists) {
            subCloner.call(projectList);
        }
    }
}
