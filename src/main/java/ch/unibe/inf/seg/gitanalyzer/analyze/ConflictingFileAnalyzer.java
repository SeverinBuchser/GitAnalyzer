package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingFileReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;

import java.io.IOException;
import java.util.ArrayList;

public class ConflictingFileAnalyzer implements Analyzer<ConflictingFile, ConflictingFileReport> {
    @Override
    public ConflictingFileReport analyze(ConflictingFile conflictingFile) {
        ConflictingFileReport report = new ConflictingFileReport(conflictingFile.getFileName());
        System.out.println(report.toString(3));

        try {
            boolean correct = false;

            ResolutionFile actualResolutionFile = conflictingFile.getActualResolutionFile();
            for (ResolutionFile resolutionFile : conflictingFile) {
                if (actualResolutionFile.compareTo(resolutionFile) == 0) {
                    correct = true;
                    break;
                }
            }

            ConflictingChunkAnalyzer subAnalyzer = new ConflictingChunkAnalyzer(actualResolutionFile);
            ArrayList<Boolean> chunks = new ArrayList<>();

            for (ConflictingChunk conflictingChunk : conflictingFile.getConflictingChunks()) {
                chunks.add(subAnalyzer.analyze(conflictingChunk));
            }

            report.ok(correct);
            report.addChunkReports(chunks);

        } catch (IOException e) {
            report.fail(e.getMessage());
        }
        System.out.println(report.toString(3));
        return report;
    }
}
