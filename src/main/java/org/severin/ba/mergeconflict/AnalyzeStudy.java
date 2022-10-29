package org.severin.ba.mergeconflict;

import org.apache.commons.csv.CSVRecord;
import org.severin.ba.util.csv.CSVFile;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "analyzestudy", mixinStandardHelpOptions = true)
public class AnalyzeStudy implements Callable<Integer> {

    @CommandLine.Parameters(index = "0") String studyPath;

    @Override
    public Integer call() {
        CSVFile updater = new CSVFile(
                new File(this.studyPath),
                new String[]{"projectName", "correctCount", "conflictCount"}
        );

        int correctCount = 0;
        int conflictCount = 0;

        for (CSVRecord record: updater.getRecords()) {
            correctCount += Integer.parseInt(record.get("correctCount"));
            conflictCount += Integer.parseInt(record.get("conflictCount"));
        }

        System.out.format("Conflict Count: %d\n", conflictCount);
        System.out.format("Correct Count: %d\n", correctCount);
        System.out.format("Correct Percentage: %f", (float) correctCount / (float) conflictCount);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new AnalyzeStudy()).execute(args);
        System.exit(exitCode);
    }
}
