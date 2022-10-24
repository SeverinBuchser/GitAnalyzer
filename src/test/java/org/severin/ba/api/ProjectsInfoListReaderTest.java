package org.severin.ba.api;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProjectsInfoListReaderTest {
    @Test
    void infoListReaderTest() {
        File file = new File(Objects.requireNonNull(ProjectsInfoListReader.class.getClassLoader()
                .getResource("project/sample-project-list.csv")).getFile());
        ProjectsInfoListReader reader = new ProjectsInfoListReader(file);
        Iterator<ProjectInfo> infos = reader.iterator();

        assertEquals("project0", infos.next().name);
        assertEquals("project1", infos.next().name);
        assertEquals("project2", infos.next().name);
        assertEquals("project3", infos.next().name);
        assertEquals("project4", infos.next().name);
        assertEquals("project5", infos.next().name);
        assertFalse(infos.hasNext());

        infos = reader.iterator();

        assertEquals("linktoproject0", infos.next().uri);
        assertEquals("linktoproject1", infos.next().uri);
        assertEquals("linktoproject2", infos.next().uri);
        assertEquals("linktoproject3", infos.next().uri);
        assertEquals("linktoproject4", infos.next().uri);
        assertEquals("linktoproject5", infos.next().uri);
        assertFalse(infos.hasNext());
    }
}