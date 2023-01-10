package ch.unibe.inf.seg.gitanalyzer.config;

import ch.unibe.inf.seg.gitanalyzer.util.file.FileHelper;
import org.everit.json.schema.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    public void emptyConfigTest() {
        Config config = new Config();
        assertTrue(config.getClone());
        assertTrue(config.getAnalyze());
        assertEquals("", config.getOutPath().toString());
        assertEquals(0, config.getProjectLists().size());
    }

    @Test
    public void defaultConfigTest() {
        this.testDefaultConfig(new Config());
    }

    private void testDefaultConfig(Config config) {
        assertTrue(config.getAnalyze());
        assertTrue(config.getClone());
        assertEquals("", config.getOutPath().toString());
        assertEquals(0, config.getProjectLists().size());
        assertEquals("""
                {
                    "analyze": true,
                    "clone": true,
                    "projectLists": [],
                    "out": ""
                }""", config.toString());
    }

    @Test
    public void invalidConfigTest() {
        JSONObject configObject = new JSONObject();
        configObject.put("invalid", "invalid");
        JSONObject projectListInfo = new ProjectList("someFile").getJsonObject();
        projectListInfo.put("invalid", "invalid");
        projectListInfo.put("list", false);
        JSONArray projectListInfos = new JSONArray();
        projectListInfos.put(projectListInfo);
        configObject.put("projectLists", projectListInfos);

        ValidationException e = assertThrows(ValidationException.class, () -> new Config(configObject));

        assertEquals("#: 3 schema violations found", e.getMessage());
        List<String> validationMessages = Config.extractValidationMessages(e);

        assertEquals("#: 3 schema violations found", e.getMessage());
        assertEquals("#: extraneous key [invalid] is not permitted", validationMessages.get(0));
        assertEquals("#/projectLists: #: only 1 subschema matches out of 2", e.getCausingExceptions().get(1).getMessage());
        assertEquals("#/projectLists/0: extraneous key [invalid] is not permitted", validationMessages.get(1));
        assertEquals("#/projectLists/0/list: expected type: String, found: Boolean", validationMessages.get(2));
    }

    @Test
    public void setValuesTest() {
        Config config = new Config();

        config.setClone(false);
        assertFalse(config.getClone());

        config.setAnalyze(false);
        assertFalse(config.getAnalyze());

        config.setOut("someDir");
        assertEquals("someDir", config.getOutPath().toString());
    }

    @Test
    public void outTest() {
        Path configPath = FileHelper.toAbsolutePath("someFile.json");
        Path outAbsolutePath = FileHelper.toAbsolutePath("");

        Config config = new Config();
        config.setConfigPath(configPath);

        // set out with relative string path
        assertEquals("", config.getOutPath().toString());
        assertEquals(outAbsolutePath.toString(), config.getOutPathAbsolute().toString());

        config.setOut("./someDir/otherDir/anotherDir/../..");
        assertEquals("someDir", config.getOutPath().toString());
        assertEquals(outAbsolutePath.resolve("someDir").toString(), config.getOutPathAbsolute().toString());

        config.setOut("./someDir/../anotherDir");
        assertEquals("anotherDir", config.getOutPath().toString());
        assertEquals(outAbsolutePath.resolve("anotherDir").toString(), config.getOutPathAbsolute().toString());


        // set out with relative Path path
        Path relativePath = Path.of("../otherDir");
        config.setOut(relativePath);
        assertEquals(relativePath.toString(), config.getOutPath().toString());
        assertEquals(FileHelper.toAbsolutePath(relativePath).toString(), config.getOutPathAbsolute().toString());


        // set out with absolute path
        Path absolutePath = FileHelper.toAbsolutePath("../otherDir");
        config.setOut(absolutePath);
        assertEquals(absolutePath.toString(), config.getOutPath().toString());
        assertEquals(absolutePath.toString(), config.getOutPathAbsolute().toString());
    }

    @Test
    public void configPathTest() {
        Path relativePath = Path.of("someFile.json");
        Path absolutePath = FileHelper.toAbsolutePath(relativePath);
        Config config = new Config();


        // set relative path
        config.setConfigPath(relativePath);
        assertEquals(relativePath.toString(), config.getConfigPath().toString());
        assertEquals(absolutePath.toString(), config.getConfigPathAbsolute().toString());

        relativePath = Path.of("./someFile.json");
        absolutePath = FileHelper.toAbsolutePath(relativePath);

        config.setConfigPath(relativePath);
        assertEquals(FileHelper.normalize(relativePath).toString(), config.getConfigPath().toString());
        assertEquals(absolutePath.toString(), config.getConfigPathAbsolute().toString());

        relativePath = Path.of("someDir/otherDir/anotherDir/../../someFile.json");
        absolutePath = FileHelper.toAbsolutePath(relativePath);

        config.setConfigPath(relativePath.toString());
        assertEquals(FileHelper.normalize(relativePath).toString(), config.getConfigPath().toString());
        assertEquals(absolutePath.toString(), config.getConfigPathAbsolute().toString());


        // set absolute path
        config.setConfigPath(absolutePath);
        assertEquals(absolutePath.toString(), config.getConfigPath().toString());
        assertEquals(absolutePath.toString(), config.getConfigPathAbsolute().toString());
    }

    @Test
    public void addProjectListsTest() {
        Config config = new Config();
        ProjectList info1 = new ProjectList("someFile");
        ProjectList info2 = new ProjectList("newFile", "dir", "suffix", true);
        ProjectLists projectLists = config.getProjectLists();

        projectLists.add(info1);
        assertEquals(1, config.getProjectLists().size());

        // dont add duplicates
        projectLists.add(info1);
        assertEquals(1, config.getProjectLists().size());

        projectLists.add(info2);
        assertEquals(2, config.getProjectLists().size());

        projectLists.remove(info1);
        assertEquals(1, config.getProjectLists().size());

        projectLists.remove(info2);
        assertEquals(0, config.getProjectLists().size());

        projectLists.add(info1);
        assertEquals(1, config.getProjectLists().size());

        projectLists.remove("someFile");
        assertEquals(0, config.getProjectLists().size());

        // do nothing, if element does not exist anymore
        projectLists.remove("someFile");
        assertEquals(0,config.getProjectLists().size());
    }

    @Test
    public void iteratorTest() {
        Config config = new Config();

        config.setClone(false);
        assertFalse(config.getClone());

        config.setAnalyze(false);
        assertFalse(config.getAnalyze());

        config.setOut("someDir");
        assertEquals("someDir", config.getOutPath().toString());

        ProjectList info1 = new ProjectList("someFile");
        ProjectList info2 = new ProjectList("newFile", "dir", "no-suffix", true);
        ProjectList info3 = new ProjectList("someOtherFile");
        ProjectLists projectLists = config.getProjectLists();
        projectLists.add(info1);
        projectLists.add(info2);
        projectLists.add(info3);
        projectLists.add(info1);

        List<ProjectList> list = new ArrayList<>();
        for (ProjectList info: projectLists) {
            list.add(info);
        }

        assertEquals("someFile", list.get(0).getListPath().toString());
        assertEquals("newFile", list.get(1).getListPath().toString());
        assertEquals("someOtherFile", list.get(2).getListPath().toString());
        assertEquals(3, list.size());
    }

    @Test
    public void testHasConfigPathTest() {
        Path path = Path.of("someFile.json");
        Config config = new Config();

        assertFalse(config.hasConfigPath());
        assertThrows(IllegalStateException.class, config::getConfigPath);
        assertThrows(IllegalStateException.class, config::getConfigPathAbsolute);

        config.setConfigPath(path);
        assertTrue(config.hasConfigPath());
    }

    @Test
    public void loadInConstructorTest() throws IOException {
        Path path = Paths.get("src","test","resources", "test.config.json");
        Config config = new Config(path);
        this.testTestConfig(config);
    }

    @Test
    public void setAndLoadTest() throws IOException {
        Path path = Paths.get("src","test","resources", "test.config.json");
        Config config = new Config();
        config.setAndLoad(path);
        this.testTestConfig(config);
    }


    @Test
    public void loadTest() throws IOException {
        Path path = Paths.get("src","test","resources", "test.config.json");
        Config config = new Config();
        config.setConfigPath(path);
        config.load();
        this.testTestConfig(config);
    }

    private void testTestConfig(Config config) {
        assertTrue(config.getAnalyze());
        assertTrue(config.getClone());
        assertEquals("out", config.getOutPath().toString());
        assertEquals(1, config.getProjectLists().size());
        assertEquals("project-list.csv", config.getProjectLists().get(0).getListPath().toString());
    }

    @Test
    public void loadNonExistingFileTest() {
        Path path = Path.of("not-existing.json");
        assertThrows(IOException.class, () -> new Config(path));

        Config config1 = new Config();
        assertThrows(IOException.class, () -> config1.setAndLoad(path));

        Config config2 = new Config();
        config2.setConfigPath(path);
        assertThrows(IOException.class, config2::load);
    }

    @Test
    public void saveTest() throws IOException {
        Path path = Paths.get("src","test","resources", "test.config.save.json");
        Config config = new Config();
        config.setConfigPath(path);
        config.save();
        // load the saved config
        config = new Config(path);
        this.testDefaultConfig(config);
        // delete the saved config again
        assertTrue(FileHelper.toAbsolutePath(path).toFile().delete());
    }

    @Test
    public void setAndSaveTest() throws IOException {
        Path path = Paths.get("src","test","resources", "test.config.save.json");
        Config config = new Config();
        config.setAndSave(path);
        // load the saved config
        config = new Config(path);
        this.testDefaultConfig(config);
        // delete the saved config again
        assertTrue(FileHelper.toAbsolutePath(path).toFile().delete());
    }

    @Test
    public void toStringTest() {
        Config config = new Config();
        assertEquals("""
                {
                    "analyze": true,
                    "clone": true,
                    "projectLists": [],
                    "out": ""
                }""", config.toString());

        config.getProjectLists().add(new ProjectList("someFile"));

        assertEquals("""
                {
                    "analyze": true,
                    "clone": true,
                    "projectLists": [{
                        "skip": false,
                        "list": "someFile",
                        "dir": "",
                        "suffix": ""
                    }],
                    "out": ""
                }""", config.toString());
    }
}