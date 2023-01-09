package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfo;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ProjectListCloner implements Cloner<ProjectList> {

    private final Logger logger;

    public ProjectListCloner(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void call(ProjectList projectList) {
        try {
            this.logger.info("Cloning Project List: " + projectList.getListPath());
            Path dir = projectList.getDirPathAbsolute();
            ProjectInfos projectInfos = projectList.toProjectInfos();
            ArrayList<CloneCommand> cloneCommands = projectInfos.toCloneCommands(dir);
            int index = 0;
            for (CloneCommand cloneCommand: cloneCommands) {
                try {
                    ProjectInfo projectInfo = projectInfos.get(index);
                    this.logger.info(1, "Cloning Project: " + projectInfo);
                    cloneCommand.call().close();
                    this.logger.success(1, "Cloned Project: " + projectInfo);
                } catch (JGitInternalException e) {
                    this.logger.info(1, e.getMessage());
                } catch (GitAPIException e) {
                    this.logger.fail(1, e.getMessage());
                }
                index++;
            }
            this.logger.success("Cloned Project List: " + projectList.getListPath());
        } catch (IOException e) {
            this.logger.fail(e.getMessage());
        }
    }
}
