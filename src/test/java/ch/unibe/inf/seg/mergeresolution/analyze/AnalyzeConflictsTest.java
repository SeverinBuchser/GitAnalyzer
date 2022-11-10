package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.helper.EmptyParameterExceptionHandler;
import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeConflictsTest {
    @Test
    public void invalidModeOptionTest() {
        CommandLine cmd = AnalyzeConflictsTest.getCmd();

        int exitCode = cmd.execute(
                "--mode=invalid",
                "--project-dir=somePath",
                "--out-dir=somePath",
                "somePath"
        );

        assertEquals(2, exitCode);
    }

    @Test
    public void missingParameterTest() {
        CommandLine cmd = AnalyzeConflictsTest.getCmd();

        int exitCode = cmd.execute(
                "--mode=merges"
        );

        assertEquals(2, exitCode);
    }

    @Test
    public void analyzeMergesTest() {
        CommandLine cmd = AnalyzeConflictsTest.getCmd();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        System.out.println();

        int exitCode = cmd.execute(
                "--project-dir=" + absolutePath,
                "--out-suffix=-test-output",
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);

        File outputFile = new File("./project-list-merges-test-output.csv");
        assertTrue(outputFile.exists());
        CSVFile output = new CSVFile(
                outputFile,
                new String[]{"projectName", "correctCount", "totalCount"}
        );
        assertEquals(1, output.getStream().toList().size());
        CSVRecord record = output.getRecords().iterator().next();
        assertEquals("samplemergeconflictproject", record.get("projectName"));
        assertEquals("0", record.get("correctCount"));
        assertEquals("4", record.get("totalCount"));
        assertTrue(outputFile.delete());
    }

    @Test
    public void analyzeFilesTest() {
        CommandLine cmd = AnalyzeConflictsTest.getCmd();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        System.out.println();

        int exitCode = cmd.execute(
                "--project-dir=" + absolutePath,
                "--mode=files",
                "--out-suffix=-test-output",
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);

        File outputFile = new File("./project-list-files-test-output.csv");
        assertTrue(outputFile.exists());
        CSVFile output = new CSVFile(
                outputFile,
                new String[]{"projectName", "correctCount", "totalCount"}
        );
        assertEquals(1, output.getStream().toList().size());
        CSVRecord record = output.getRecords().iterator().next();
        assertEquals("samplemergeconflictproject", record.get("projectName"));
        assertEquals("0", record.get("correctCount"));
        assertEquals("5", record.get("totalCount"));
        assertTrue(outputFile.delete());
    }

    @Test
    public void analyzeConflictsTest() {
        CommandLine cmd = AnalyzeConflictsTest.getCmd();

        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        System.out.println();

        int exitCode = cmd.execute(
                "--project-dir=" + absolutePath,
                "--mode=chunks",
                "--out-suffix=-test-output",
                absolutePath + "/project-list.csv"
        );

        assertEquals(0, exitCode);

        File outputFile = new File("./project-list-chunks-test-output.csv");
        assertTrue(outputFile.exists());
        CSVFile output = new CSVFile(
                outputFile,
                new String[]{"projectName", "correctCount", "totalCount"}
        );
        assertEquals(1, output.getStream().toList().size());
        CSVRecord record = output.getRecords().iterator().next();
        assertEquals("samplemergeconflictproject", record.get("projectName"));
        assertEquals("5", record.get("correctCount"));
        assertEquals("7", record.get("totalCount"));
        assertTrue(outputFile.delete());
    }

    private static CommandLine getCmd() {
        AnalyzeConflicts analyzeConflicts = new AnalyzeConflicts();
        return new CommandLine(analyzeConflicts).setParameterExceptionHandler(new EmptyParameterExceptionHandler());
    }
}