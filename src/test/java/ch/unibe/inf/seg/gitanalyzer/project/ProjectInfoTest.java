package ch.unibe.inf.seg.gitanalyzer.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectInfoTest {
    @Test
    void simpleProjectInfoTest() {
        ProjectInfo info = new ProjectInfo("projectName", "projectUri");
        assertEquals("projectName", info.getName());
        assertEquals("projectUri", info.getRemoteURL());
    }
}