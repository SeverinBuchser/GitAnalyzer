package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingChunk;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingFileReport;
import ch.unibe.inf.seg.gitanalyzer.resolution.ResolutionFile;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

import java.io.IOException;

public class ConflictingFileAnalyzer extends AbstractAnalyzer<ConflictingFile, ConflictingFileReport> {
    private final ConflictingChunkAnalyzer subAnalyzer;

    public ConflictingFileAnalyzer(Logger logger) {
        this(logger, 0);
    }

    public ConflictingFileAnalyzer(Logger logger, int level) {
        super(logger, level);
        this.subAnalyzer = new ConflictingChunkAnalyzer(logger, level + 1);
    }

    @Override
    public ConflictingFileReport call(ConflictingFile conflictingFile) {
        ConflictingFileReport report = new ConflictingFileReport(conflictingFile.getFileName());
        this.logReport(report);

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
        this.logReport(report);
        return report;
    }
}
