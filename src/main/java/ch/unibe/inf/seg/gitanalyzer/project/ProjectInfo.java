package ch.unibe.inf.seg.gitanalyzer.project;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.nio.file.Path;

public class ProjectInfo {
    private final String name;

    private final String remoteURL;

    public String getName() {
        return name;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public ProjectInfo(String name, String remoteURL) {
        this.name = name;
        this.remoteURL = remoteURL;
    }

    /**
     * Creates a {@link CloneCommand} for a git project.
     * The command, when called, clones a git project to a local directory. The project will not be cloned directly to
     * the directory but rather a subdirectory with the same name as the project. Example:
     * dir - "some/directory"
     * name - "the/project/name"
     * This would clone the project to: "some/directory/the/project/name".
     *
     * @param dir The parent directory of the project.
     * @return A {@link CloneCommand} to clone the project.
     */
    public CloneCommand toCloneCommand(Path dir) {
        return Git.cloneRepository()
                .setURI(this.remoteURL)
                .setDirectory(this.toGirDir(dir).toFile());
    }

    /**
     * Creates a project from a local directory.
     * The project will not be loaded from the directory directly but rather a subdirectory with the same name as the
     * project. Example:
     * dir - "some/directory"
     * name - "the/project/name"
     * This loads the project from: "some/directory/the/project/name".
     *
     * @param dir The parent directory of the project.
     * @return The loaded project.
     * @throws IOException the project could not be loaded or does not exist.
     */
    public Project toProject(Path dir) throws IOException {
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(this.toGirDir(dir).resolve(".git").toFile())
                .build();
        return new Project(repo, this.name);
    }

    /**
     * Resolves the directory of the project in a parent directory.
     * @param dir The parent directory of this project.
     * @return The resolved directory.
     */
    private Path toGirDir(Path dir) {
        return dir.resolve(this.name);
    }

    @Override
    public String toString() {
        return String.format("Project: %s, remote URL: %s", this.name, this.remoteURL);
    }
}
