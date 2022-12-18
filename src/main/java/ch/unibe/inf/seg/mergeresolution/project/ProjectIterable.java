package ch.unibe.inf.seg.mergeresolution.project;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class ProjectIterable implements Iterable<Project> {
    private final ProjectsInfoListReader reader;
    private final String projectDir;

    public ProjectIterable(ProjectsInfoListReader reader, String projectDir) {
        this.reader = reader;
        this.projectDir = projectDir;
    }

    @Override
    public Iterator<Project> iterator() {
        return new Iterator<>() {
            private final Iterator<ProjectInfo> projectInfoIterator = reader.iterator();

            @Override
            public boolean hasNext() {
                return projectInfoIterator.hasNext();
            }

            @Override
            public Project next() {
                String name = projectInfoIterator.next().name;
                try {
                    return Project.buildFromPath(
                            Paths.get(projectDir, FilenameUtils.separatorsToSystem(name)),
                            name
                    );
                } catch (IOException e) {
                    return null;
                }
            }
        };
    }
}
