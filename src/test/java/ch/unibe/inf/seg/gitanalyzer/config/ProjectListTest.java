package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ProjectListTest {
    @Test
    public void defaultProjectListTest() {
        ProjectList projectList = new ProjectList("testList.csv");
        assertFalse(projectList.hasProjectLists());
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
        assertFalse(projectList.hasProjectLists());
        assertEquals("testList.json", projectList.getOutFilename().toString());
    }

    public ProjectList createProjectListWithConfig(Path configPath, String list) {
        Config config = new Config();
        config.setConfigPath(configPath);
        ProjectList projectList = new ProjectList(list);
        config.getProjectLists().add(projectList);
        return projectList;
    }

    @Test
    public void defaultProjectListWithInProjectListsTest() {
        Path configPath = Path.of("configs");
        Path list = Path.of("../lists/testList.csv");
        ProjectList projectList = this.createProjectListWithConfig(configPath, list.toString());

        assertTrue(projectList.hasProjectLists());
        assertTrue(projectList.getProjectLists().belongsToConfig());

        assertEquals(list.toString(), projectList.getListPath().toString());
        assertEquals(
                FileHelper.toAbsolutePath(configPath.resolve(list)).toString(),
                projectList.getListPathAbsolute().toString()
        );
        // dir == configPath
        assertEquals("", projectList.getDirPath().toString());
        assertEquals(
                FileHelper.toAbsolutePath(configPath).toString(),
                projectList.getDirPathAbsolute().toString()
        );
        assertEquals("", projectList.getSuffix());
        assertFalse(projectList.getSkip());
        assertEquals("testList.json", projectList.getOutFilename().toString());
    }

    @Test
    public void absoluteListTest() {
        Path configPath = Path.of("configs");
        Path list = Path.of("/absolute/lists/testList.csv");
        ProjectList projectList = this.createProjectListWithConfig(configPath, list.toString());
        assertEquals(list.toString(), projectList.getListPath().toString());
        assertEquals(list.toString(), projectList.getListPathAbsolute().toString());

        list = Path.of("/absolute/lists/newList.csv");
        projectList.setList(list);
        assertEquals(list.toString(), projectList.getListPath().toString());
        assertEquals(list.toString(), projectList.getListPathAbsolute().toString());
    }

    @Test
    public void absoluteDirTest() {
        Path configPath = Path.of("configs");
        Path list = Path.of("/absolute/lists/testList.csv");
        Path dir = Path.of("/absolute/dir");
        ProjectList projectList = this.createProjectListWithConfig(configPath, list.toString());
        projectList.setDir(dir);
        assertEquals(dir.toString(), projectList.getDirPath().toString());
        assertEquals(dir.toString(), projectList.getDirPathAbsolute().toString());
    }

    @Test
    public void constructionTests() {
        ProjectLists projectLists = new ProjectLists();
        ProjectList projectList = new ProjectList("", projectLists);
        assertTrue(projectList.hasProjectLists());
        assertFalse(projectLists.has(projectList));
        projectLists.add(projectList);
        assertTrue(projectLists.has(projectList));

        projectLists = new ProjectLists();
        projectList = new ProjectList("list", projectLists);
        assertTrue(projectList.hasProjectLists());
        assertFalse(projectLists.has(projectList));
        projectLists.add(projectList);
        assertTrue(projectLists.has(projectList));

        projectLists = new ProjectLists();
        projectList = new ProjectList("list", "dir", "suffix", true, projectLists);
        assertTrue(projectList.hasProjectLists());
        assertFalse(projectLists.has(projectList));
        projectLists.add(projectList);
        assertTrue(projectLists.has(projectList));

        projectList = new ProjectList("list", "dir", "suffix", true);
        assertFalse(projectList.hasProjectLists());
        assertEquals("list", projectList.getListPath().toString());
        assertEquals("dir", projectList.getDirPath().toString());
        assertEquals("suffix", projectList.getSuffix());
        assertTrue(projectList.getSkip());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", "list");
        jsonObject.put("dir", "dir");
        projectList = new ProjectList(jsonObject);
        assertEquals("list", projectList.getListPath().toString());
        assertEquals("dir", projectList.getDirPath().toString());
        assertEquals("", projectList.getSuffix());
        assertFalse(projectList.getSkip());

        jsonObject = new JSONObject();
        jsonObject.put("list", "list");
        jsonObject.put("dir", "dir");
        projectLists = new ProjectLists();
        projectList = new ProjectList(jsonObject, projectLists);
        assertEquals("list", projectList.getListPath().toString());
        assertEquals("dir", projectList.getDirPath().toString());
        assertEquals("", projectList.getSuffix());
        assertFalse(projectList.getSkip());
        assertTrue(projectList.hasProjectLists());
        assertFalse(projectLists.has(projectList));
        projectLists.add(projectList);
        assertTrue(projectLists.has(projectList));
    }

    @Test
    public void toStringTest() {
        ProjectList projectList = new ProjectList("list", "dir", "suffix", true);
        assertEquals("""
                {
                    "skip": true,
                    "list": "list",
                    "dir": "dir",
                    "suffix": "suffix"
                }""", projectList.toString());
    }

    // TODO: to project infos test
}