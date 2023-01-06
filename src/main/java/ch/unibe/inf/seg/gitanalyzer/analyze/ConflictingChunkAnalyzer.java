package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingChunkReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import ch.unibe.inf.seg.gitanalyzer.util.logger.AnalyzerLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.OutputStreamLogger;

public class ConflictingChunkAnalyzer implements Analyzer<ConflictingChunk, ConflictingChunkReport> {
    private ResolutionFile resolutionFile;

    private AnalyzerLogger logger = new OutputStreamLogger(System.out, 4);

    public ConflictingChunkAnalyzer() {}

    public ConflictingChunkAnalyzer(AnalyzerLogger logger) {
        this.logger = logger;
    }

    public void setResolutionFile(ResolutionFile resolutionFile) {
        this.resolutionFile = resolutionFile;
    }

    @Override
    public ConflictingChunkReport analyze(ConflictingChunk conflictingChunk) {
        if (this.resolutionFile == null) throw new IllegalStateException("The resolution file has not been set yet.");
        ConflictingChunkReport report = new ConflictingChunkReport();
        this.logger.println(report, 4);
        boolean correct = false;

        if (conflictingChunk.isFirstConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        } else if (conflictingChunk.isNextConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        }
        report.ok(correct);

        this.logger.println(report, 4);
        return report;
    }
}
