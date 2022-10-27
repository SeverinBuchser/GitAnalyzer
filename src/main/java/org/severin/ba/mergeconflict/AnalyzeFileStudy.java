package org.severin.ba.mergeconflict;

import org.apache.commons.csv.CSVRecord;
import org.severin.ba.util.csv.CSVFile;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "analyzefilestudy", mixinStandardHelpOptions = true)
public class AnalyzeFileStudy implements Callable<Integer> {

    @CommandLine.Parameters(index = "0") String studyPath;

    @Override
    public Integer call() {
        CSVFile updater = new CSVFile(
                new File(this.studyPath),
                new String[]{"projectName", "conflictCount", "conflictingFileCount"}
        );

        int correctCount = 0;
        int conflictingFileCount = 0;

        for (CSVRecord record: updater.getRecords()) {
            correctCount += Integer.parseInt(record.get("conflictCount"));
            conflictingFileCount += Integer.parseInt(record.get("conflictingFileCount"));
        }

        System.out.format("Conflict File Count: %d\n", conflictingFileCount);
        System.out.format("Correct Count: %d\n", correctCount);
        System.out.format("Correct Percentage: %f", (float) correctCount / (float) conflictingFileCount);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new AnalyzeFileStudy()).execute(args);
        System.exit(exitCode);
    }
}
