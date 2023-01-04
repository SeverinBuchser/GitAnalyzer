package ch.unibe.inf.seg.gitanalyzer.project;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectLists;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;


/**
 * {@link Iterator} for projects.
 * Each of the projects is created dynamically, which can lead to a {@link RuntimeException}. The exception will be
 * thrown if a project could not be loaded.
 */
public class ProjectIterator implements Iterator<Project> {
    private final Iterator<ProjectInfo> innerIterator;
    private final Path dir;
    public final int size;

    /**
     * Constructor.
     * @param projectInfos The {@link ProjectLists}
     * @param dir The parent directory of the projects.
     */
    public ProjectIterator(ProjectInfos projectInfos, Path dir, int size) {
        this.innerIterator = projectInfos.iterator();
        this.dir = dir;
        this.size = size;
    }

    @Override
    public boolean hasNext() {
        return this.innerIterator.hasNext();
    }

    /**
     * Creates and returns the next {@link Project}.
     * @return The next {@link Project}.
     * @throws RuntimeException the project could not be loaded or does not exist.
     * @see ProjectInfo#toProject(Path)
     */
    @Override
    public Project next() {
        try {
            return this.innerIterator.next().toProject(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
