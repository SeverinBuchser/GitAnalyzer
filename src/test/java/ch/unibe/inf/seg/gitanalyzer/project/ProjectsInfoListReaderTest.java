package ch.unibe.inf.seg.gitanalyzer.project;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProjectsInfoListReaderTest {
    @Test
    void infoListReaderTest() throws IOException {
        File file = new File(Objects.requireNonNull(ProjectsInfoListReader.class.getClassLoader()
                .getResource("sample-project-list.csv")).getFile());
        ProjectsInfoListReader reader = ProjectsInfoListReader.read(file);
        Iterator<ProjectInfo> infos = reader.iterator();

        assertEquals("project0", infos.next().name);
        assertEquals("project1", infos.next().name);
        assertEquals("project2", infos.next().name);
        assertEquals("project3", infos.next().name);
        assertEquals("project4", infos.next().name);
        assertEquals("project5", infos.next().name);
        assertFalse(infos.hasNext());

        infos = reader.iterator();

        assertEquals("linktoproject0", infos.next().url);
        assertEquals("linktoproject1", infos.next().url);
        assertEquals("linktoproject2", infos.next().url);
        assertEquals("linktoproject3", infos.next().url);
        assertEquals("linktoproject4", infos.next().url);
        assertEquals("linktoproject5", infos.next().url);
        assertFalse(infos.hasNext());
    }
}