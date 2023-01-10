package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingChunkReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

public class ConflictingChunkAnalyzer extends AbstractAnalyzer<ConflictingChunk, ConflictingChunkReport> {
    private ResolutionFile resolutionFile;

    public ConflictingChunkAnalyzer(Logger logger, int level) {
        super(logger, level);
    }

    public void setResolutionFile(ResolutionFile resolutionFile) {
        this.resolutionFile = resolutionFile;
    }

    @Override
    public ConflictingChunkReport call(ConflictingChunk conflictingChunk) {
        if (this.resolutionFile == null) throw new IllegalStateException("The resolution file has not been set yet.");
        ConflictingChunkReport report = new ConflictingChunkReport();
        this.logReport(report);
        boolean correct = false;

        if (conflictingChunk.isFirstConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        } else if (conflictingChunk.isNextConflictingRangeInResolutionFile(this.resolutionFile)) {
            correct = true;
        }
        report.ok(correct);

        this.logReport(report);
        return report;
    }
}
