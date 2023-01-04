package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingFileReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import ch.unibe.inf.seg.gitanalyzer.util.logger.AnalyzerLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.SimpleAnalyzerLogger;

import java.io.IOException;

public class ConflictingFileAnalyzer implements Analyzer<ConflictingFile, ConflictingFileReport> {

    private AnalyzerLogger logger = new SimpleAnalyzerLogger(System.out, 3);
    private final ConflictingChunkAnalyzer subAnalyzer;

    public ConflictingFileAnalyzer() {
        this.subAnalyzer = new ConflictingChunkAnalyzer(this.logger);
    }

    public ConflictingFileAnalyzer(AnalyzerLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ConflictingChunkAnalyzer(this.logger);
    }

    @Override
    public ConflictingFileReport analyze(ConflictingFile conflictingFile) {
        ConflictingFileReport report = new ConflictingFileReport(conflictingFile.getFileName());
        this.logger.println(report, 3);

        try {
            boolean correct = false;

            ResolutionFile actualResolutionFile = conflictingFile.getActualResolutionFile();
            for (ResolutionFile resolutionFile : conflictingFile) {
                if (actualResolutionFile.compareTo(resolutionFile) == 0) {
                    correct = true;
                    break;
                }
            }

            this.subAnalyzer.setResolutionFile(actualResolutionFile);

            for (ConflictingChunk conflictingChunk : conflictingFile.getConflictingChunks()) {
                report.addChunkReport(this.subAnalyzer.analyze(conflictingChunk));
            }

            report.ok(correct);

        } catch (IOException e) {
            report.fail(e.getMessage());
        }
        this.logger.println(report, 3);
        return report;
    }
}
