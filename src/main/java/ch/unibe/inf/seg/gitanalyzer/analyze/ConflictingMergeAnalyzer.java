package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingFile;
import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import ch.unibe.inf.seg.gitanalyzer.error.NotComparableMergesException;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingFileReport;
import ch.unibe.inf.seg.gitanalyzer.report.ConflictingMergeReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.PrintStreamLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.ReportLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.PrintStreamReportLogger;

import java.io.IOException;

public class ConflictingMergeAnalyzer implements Analyzer<ConflictingMerge, ConflictingMergeReport> {

    private static final int CONFLICT_LIMIT = 12;

    private ReportLogger logger = new PrintStreamReportLogger(new PrintStreamLogger(System.out), 2);


    private final ConflictingFileAnalyzer subAnalyzer;

    public ConflictingMergeAnalyzer() {
        this.subAnalyzer = new ConflictingFileAnalyzer(this.logger);
    }

    public ConflictingMergeAnalyzer(ReportLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ConflictingFileAnalyzer(this.logger);
    }

    public ConflictingMergeReport call(ConflictingMerge conflictingMerge) {
        ConflictingMergeReport report = new ConflictingMergeReport(conflictingMerge.getCommitId());
        this.logger.report(report, 2);

        try {
            if (checkConflictCount(conflictingMerge)) {
                report.skip("Binary Files");
            } else if (checkConflictLimit(conflictingMerge)) {
                report.skip(getTooManyConflictsMessage(conflictingMerge));
            } else {
                int conflictingFileCorrectCount = 0;
                int conflictingFileCount = 0;

                for (ConflictingFile conflictingFile: conflictingMerge.getConflictingFiles()) {
                    ConflictingFileReport fileReport = this.subAnalyzer.call(conflictingFile);
                    if (fileReport.isFail() || fileReport.isSkip()) {
                        throw new IOException("Unwanted exception");
                    }

                    if (fileReport.isMarked()) conflictingFileCorrectCount++;
                    conflictingFileCount++;
                    report.addFileReport(fileReport);
                }

                report.ok(conflictingFileCorrectCount == conflictingFileCount);
            }
        } catch (IOException e) {
            report.fail(e.getMessage());
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NotComparableMergesException) {
                report.fail(e.getMessage());
            } else throw e;
        }

        this.logger.report(report, 2);
        return report;
    }

    private static boolean checkConflictCount(ConflictingMerge conflictingMerge) {
        if (conflictingMerge.getConflictCount() == 0) return true;
        for (ConflictingFile conflictingFile: conflictingMerge.getConflictingFiles()) {
            if (conflictingFile.getConflictCount() == 0) return true;
        }
        return  false;
    }

    private static boolean checkConflictLimit(ConflictingMerge conflictingMerge) {
        return conflictingMerge.getConflictCount() > CONFLICT_LIMIT;
    }

    private static String getTooManyConflictsMessage(ConflictingMerge conflictingMerge) {
        return String.format("Conflicts: %.0f, Max: %d", conflictingMerge.getConflictCount(), CONFLICT_LIMIT);
    }
}

