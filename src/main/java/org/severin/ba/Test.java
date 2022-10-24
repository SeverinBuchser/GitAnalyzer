package org.severin.ba;

import org.apache.commons.csv.CSVRecord;
import org.severin.ba.util.log.CSVFile;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        CSVFile updater = new CSVFile(
                "/home/severin/gitrepo/Severin/UniBe/MergeConflictResolution/study-2.csv",
                new String[]{"projectName", "correct", "conflicts"}
        );

        int correctCount = 0;
        int conflictCount = 0;

        for (CSVRecord record: updater.getRecords()) {
            correctCount += Integer.parseInt(record.get("correct"));
            conflictCount += Integer.parseInt(record.get("conflicts"));
        }

        System.out.format("Conflict Count: %d\n", conflictCount);
        System.out.format("Correct Count: %d\n", correctCount);
        System.out.format("Correct Percentage: %f", (float) correctCount / (float) conflictCount);
    }

}
