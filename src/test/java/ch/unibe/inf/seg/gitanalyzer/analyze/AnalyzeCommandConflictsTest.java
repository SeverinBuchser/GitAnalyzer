package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.cli.AnalyzeCommand;
import ch.unibe.inf.seg.gitanalyzer.helper.EmptyParameterExceptionHandler;
import ch.unibe.inf.seg.gitanalyzer.helper.TestingPrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeCommandTest {
    @Test
    public void missingParameterTest() {
        CommandLine cmd = AnalyzeCommandTest.getAnalyzer();

        int exitCode = cmd.execute(
                "--mode=merges"
        );

        assertEquals(2, exitCode);
    }

    @Test
    public void analyzeProjectListTest() throws IOException {
        System.setOut(new TestingPrintStream());
        CommandLine cmd = AnalyzeCommandTest.getAnalyzer();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        System.out.println();

        int exitCode = cmd.execute(
                "--dir=" + absolutePath,
                "--suffix=test-output",
                "--verbose=true",
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);

        File outputFile = new File("./project-list-test-output.json");

        assertTrue(outputFile.exists());
        String jsonString = Files.readString(outputFile.toPath());



        // check whole list output
        JSONObject output = new JSONObject(jsonString);
        assertEquals("project_list", output.getString("id"));
        assertEquals("OK", output.getString("state"));
        assertEquals(absolutePath + "/project-list.csv", output.getString("list"));



        // check projects
        JSONArray projects = output.getJSONArray("projects");
        assertEquals(1, projects.length());

        JSONObject project = projects.getJSONObject(0);
        assertEquals("project", project.getString("id"));
        assertEquals("OK", project.getString("state"));
        assertEquals("samplemergeconflictproject", project.getString("project_name"));
        assertEquals(24, project.getInt("commit_count"));
        assertEquals(6, project.getInt("merge_count"));
        assertEquals(0, project.getInt("octopus_merge_count"));
        assertEquals(0, project.getInt("tag_count"));
        assertEquals(3, project.getInt("contributor_count"));



        // check conflicting merges
        JSONArray cms = project.getJSONArray("cms");
        assertEquals(4, cms.length());

        JSONObject cm1 = cms.getJSONObject(0);
        assertEquals("cm", cm1.getString("id"));
        assertEquals("SKIP", cm1.getString("state"));
        assertEquals("16793e9c9192a5f86d4523bb9ecdd33c20ab9305", cm1.getString("commit_id"));

        JSONObject cm2 = cms.getJSONObject(1);
        assertEquals("cm", cm2.getString("id"));
        assertEquals("OK", cm2.getString("state"));
        assertEquals("8093b82e8ee92795f8f4dade96cf90de2f31c24c", cm2.getString("commit_id"));
        assertFalse(cm2.getBoolean("mark"));

        JSONObject cm3 = cms.getJSONObject(2);
        assertEquals("cm", cm3.getString("id"));
        assertEquals("OK", cm3.getString("state"));
        assertEquals("4d384506fe3d587e2465e639d21867bd7197dad4", cm3.getString("commit_id"));
        assertFalse(cm3.getBoolean("mark"));

        JSONObject cm4 = cms.getJSONObject(3);
        assertEquals("cm", cm4.getString("id"));
        assertEquals("OK", cm4.getString("state"));
        assertEquals("70cf0e42600be4e71945c1102cb5a85ef6bbde82", cm4.getString("commit_id"));
        assertFalse(cm4.getBoolean("mark"));



        // check conflicting files
        assertFalse(cm1.has("cfs"));

        JSONArray cfs2 = cm2.getJSONArray("cfs");
        assertEquals(1, cfs2.length());

        JSONObject cf2 = cfs2.getJSONObject(0);
        assertEquals("cf", cf2.getString("id"));
        assertEquals("OK", cf2.getString("state"));
        assertEquals("src/main/Main.java", cf2.getString("file_name"));
        assertFalse(cm4.getBoolean("mark"));

        JSONArray cfs3 = cm3.getJSONArray("cfs");
        assertEquals(1, cfs3.length());

        JSONObject cf3 = cfs3.getJSONObject(0);
        assertEquals("cf", cf3.getString("id"));
        assertEquals("OK", cf3.getString("state"));
        assertEquals("src/main/Main.java", cf3.getString("file_name"));
        assertFalse(cf3.getBoolean("mark"));

        JSONArray cfs4 = cm4.getJSONArray("cfs");
        assertEquals(1, cfs3.length());

        JSONObject cf4 = cfs4.getJSONObject(0);
        assertEquals("cf", cf4.getString("id"));
        assertEquals("OK", cf4.getString("state"));
        assertEquals("src/main/Main.java", cf4.getString("file_name"));
        assertFalse(cf4.getBoolean("mark"));



        // check conflicting chunks
        JSONArray ccs2 = cf2.getJSONArray("ccs");
        assertEquals(2, ccs2.length());

        JSONObject cc20 = ccs2.getJSONObject(0);
        assertEquals("cc", cc20.getString("id"));
        assertEquals("OK", cc20.getString("state"));
        assertTrue(cc20.getBoolean("mark"));

        JSONObject cc21 = ccs2.getJSONObject(1);
        assertEquals("cc", cc21.getString("id"));
        assertEquals("OK", cc21.getString("state"));
        assertFalse(cc21.getBoolean("mark"));

        JSONArray ccs3 = cf3.getJSONArray("ccs");
        assertEquals(2, ccs3.length());

        JSONObject cc30 = ccs3.getJSONObject(0);
        assertEquals("cc", cc30.getString("id"));
        assertEquals("OK", cc30.getString("state"));
        assertTrue(cc30.getBoolean("mark"));

        JSONObject cc31 = ccs3.getJSONObject(1);
        assertEquals("cc", cc31.getString("id"));
        assertEquals("OK", cc31.getString("state"));
        assertTrue(cc31.getBoolean("mark"));

        JSONArray ccs4 = cf4.getJSONArray("ccs");
        assertEquals(2, ccs4.length());

        JSONObject cc40 = ccs4.getJSONObject(0);
        assertEquals("cc", cc40.getString("id"));
        assertEquals("OK", cc40.getString("state"));
        assertTrue(cc40.getBoolean("mark"));

        JSONObject cc41 = ccs4.getJSONObject(1);
        assertEquals("cc", cc41.getString("id"));
        assertEquals("OK", cc41.getString("state"));
        assertFalse(cc41.getBoolean("mark"));

        assertTrue(outputFile.delete());
    }

    private static CommandLine getAnalyzer() {
        AnalyzeCommand analyzeCommand = new AnalyzeCommand();
        return new CommandLine(analyzeCommand).setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }
}