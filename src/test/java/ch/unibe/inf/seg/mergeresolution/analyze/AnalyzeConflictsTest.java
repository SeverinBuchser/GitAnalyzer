package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.clone.CloneProjects;
import ch.unibe.inf.seg.mergeresolution.helper.EmptyParameterExceptionHandler;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyzeConflictsTest {

    @BeforeAll
    public static void cloneSampleProject() {
        CommandLine cmd = AnalyzeConflictsTest.getCloner();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        int exitCode = cmd.execute(
                "--project-dir=" + absolutePath,
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);
    }

    @Test
    public void missingParameterTest() {
        CommandLine cmd = AnalyzeConflictsTest.getAnalyzer();

        int exitCode = cmd.execute(
                "--mode=merges"
        );

        assertEquals(2, exitCode);
    }

    @Test
    public void analyzeProjectListTest() throws IOException {
        CommandLine cmd = AnalyzeConflictsTest.getAnalyzer();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        System.out.println();

        int exitCode = cmd.execute(
                "--project-dir=" + absolutePath,
                "--out-suffix=test-output",
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);

        File outputFile = new File("./project-list-test-output.json");

        assertTrue(outputFile.exists());
        String jsonString = Files.readString(outputFile.toPath());
        JSONObject output = new JSONObject(jsonString);
        assertEquals(1, output.getJSONArray("projects").length());
        assertEquals("samplemergeconflictproject", output.getJSONArray("projects").getJSONObject(0).getString("project_name"));

        assertEquals("OK", output.getString("state"));
        assertEquals(1, output.getInt("projects_count"));
        assertEquals(0, output.getInt("projects_correct_count"));
        assertEquals(4, output.getInt("conflicting_merges_count"));
        assertEquals(0, output.getInt("conflicting_merges_correct_count"));
        assertEquals(4, output.getInt("conflicting_files_count"));
        assertEquals(0, output.getInt("conflicting_files_correct_count"));
        assertEquals(7, output.getInt("conflicting_chunks_count"));
        assertEquals(5, output.getInt("conflicting_chunks_correct_count"));

        assertEquals(7, output.getInt("conflict_count"));
        assertEquals(24, output.getInt("commits_count"));
        assertEquals(6, output.getInt("merges_count"));
        assertEquals(0, output.getInt("tags_count"));
        assertEquals(3, output.getInt("contributors_count"));

        assertTrue(outputFile.delete());
    }

    private static CommandLine getAnalyzer() {
        AnalyzeConflicts analyzeConflicts = new AnalyzeConflicts();
        return new CommandLine(analyzeConflicts).setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }

    private static CommandLine getCloner() {
        CloneProjects cloneProjects = new CloneProjects();
        return new CommandLine(cloneProjects).setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }
}