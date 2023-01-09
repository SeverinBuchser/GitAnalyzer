package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ProjectListCloner implements Cloner<ProjectList> {
    @Override
    public void call(ProjectList projectList) {
        try {
            Path dir = projectList.getDirPathAbsolute();
            ProjectInfos projectInfos = projectList.toProjectInfos();
            ArrayList<CloneCommand> cloneCommands = projectInfos.toCloneCommands(dir);
            for (CloneCommand cloneCommand: cloneCommands) {
                try {
                    cloneCommand.call().close();
                } catch (InvalidRemoteException e) {
                    // TODO: logger
                } catch (TransportException e) {
                    // TODO: logger
                } catch (GitAPIException e) {
                    // TODO: logger
                } catch (RuntimeException e) {
                    // TODO: logger
                }
            }
        } catch (IOException e) {
            // TODO: logger
        }
    }
}
