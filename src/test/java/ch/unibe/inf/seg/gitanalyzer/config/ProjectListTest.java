package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectListTest {
    @Test
    public void emptyProjectListTest() {
        ProjectList projectList = new ProjectList();
        assertFalse(projectList.hasList());
        assertFalse(projectList.belongsToProjectLists());
        assertThrows(IllegalStateException.class, projectList::getListPath);
        assertThrows(IllegalStateException.class, projectList::getListPathAbsolute);
        assertThrows(IllegalStateException.class, projectList::getDirPath);
        assertThrows(IllegalStateException.class, projectList::getDirPathAbsolute);
        assertThrows(IllegalStateException.class, projectList::getSuffix);
        assertThrows(IllegalStateException.class, projectList::getSkip);
        assertThrows(IllegalStateException.class, projectList::getProjectLists);
        assertThrows(IllegalStateException.class, projectList::getOutFilename);
        assertThrows(IllegalStateException.class, projectList::toProjectInfos);
    }

    @Test
    public void defaultProjectListTest() {
        ProjectList projectList = new ProjectList("testList.csv");
        assertTrue(projectList.hasList());
        assertFalse(projectList.belongsToProjectLists());
        // list == cwd + list
        assertEquals("testList.csv", projectList.getListPath().toString());
        assertEquals(
                FileHelper.toAbsolutePath("testList.csv").toString(),
                projectList.getListPathAbsolute().toString()
        );
        // dir == cwd
        assertEquals("", projectList.getDirPath().toString());
        assertEquals(FileHelper.toAbsolutePath("").toString(), projectList.getDirPathAbsolute().toString());
        assertEquals("", projectList.getSuffix());
        assertFalse(projectList.getSkip());
        assertThrows(IllegalStateException.class, projectList::getProjectLists);
        assertEquals("testList.json", projectList.getOutFilename().toString());
    }
}