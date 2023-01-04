package ch.unibe.inf.seg.gitanalyzer.clone;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectLists;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Cloner {

    private final ArrayList<ProjectList> projectLists = new ArrayList<>();

    public Cloner() {}

    public Cloner(ProjectList projectList) {
        this.projectLists.add(projectList);
    }

    public Cloner addProjectLists(ProjectLists projectLists) {
        for (ProjectList projectList: projectLists) {
            this.projectLists.add(projectList);
        }
        return this;
    }

    public void call() throws IOException {
        for (ProjectList projectList: this.projectLists) {
            Path dir = projectList.getDirRelative();
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
        }
    }
}
