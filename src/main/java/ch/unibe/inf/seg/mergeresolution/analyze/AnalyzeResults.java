package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.util.csv.CSVFile;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "analyze-results", mixinStandardHelpOptions = true)
public class AnalyzeResults implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "Path to the results file to analyze."
    )
    String resultsFilePath;

    private void normalizePaths() {
        this.resultsFilePath = FilenameUtils.separatorsToSystem(this.resultsFilePath);
        this.resultsFilePath = Paths.get(this.resultsFilePath).normalize().toAbsolutePath().toString();
    }

    @Override
    public Integer call() {
        this.normalizePaths();

        CSVFile updater = new CSVFile(
                new File(this.resultsFilePath),
                new String[]{"projectName", "correctCount", "totalCount"}
        );

        System.out.format("Analyzing results file %s\n", this.resultsFilePath);

        int correctCount = 0;
        int totalCount = 0;

        for (CSVRecord record: updater.getRecords()) {
            correctCount += Integer.parseInt(record.get("correctCount"));
            totalCount += Integer.parseInt(record.get("totalCount"));
        }

        System.out.format("Total Count: %d\n", totalCount);
        System.out.format("Correct Count: %d\n", correctCount);
        System.out.format("Correct Percentage: %f", (float) correctCount / (float) totalCount);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new AnalyzeResults()).execute(args);
        System.exit(exitCode);
    }
}
