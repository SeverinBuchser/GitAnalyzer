package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingFileReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import ch.unibe.inf.seg.gitanalyzer.util.logger.PrintStreamLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.ReportLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.PrintStreamReportLogger;

import java.io.IOException;

public class ConflictingFileAnalyzer implements Analyzer<ConflictingFile, ConflictingFileReport> {

    private ReportLogger logger = new PrintStreamReportLogger(new PrintStreamLogger(System.out), 3);

    private final ConflictingChunkAnalyzer subAnalyzer;

    public ConflictingFileAnalyzer() {
        this.subAnalyzer = new ConflictingChunkAnalyzer(this.logger);
    }

    public ConflictingFileAnalyzer(ReportLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ConflictingChunkAnalyzer(this.logger);
    }

    @Override
    public ConflictingFileReport call(ConflictingFile conflictingFile) {
        ConflictingFileReport report = new ConflictingFileReport(conflictingFile.getFileName());
        this.logger.report(report, 3);

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
                report.addChunkReport(this.subAnalyzer.call(conflictingChunk));
            }

            report.ok(correct);

        } catch (IOException e) {
            report.fail(e.getMessage());
        }
        this.logger.report(report, 3);
        return report;
    }
}
