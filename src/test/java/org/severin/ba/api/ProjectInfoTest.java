package org.severin.ba.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectInfoTest {
    @Test
    void simpleProjectInfoTest() {
        ProjectInfo info = new ProjectInfo("projectName", "projectUri");
        assertEquals("projectName", info.name);
        assertEquals("projectUri", info.uri);
    }
}