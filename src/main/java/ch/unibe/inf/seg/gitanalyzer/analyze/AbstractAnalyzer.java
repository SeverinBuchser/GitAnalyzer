package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.report.Report;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

public abstract class AbstractAnalyzer<T, R> implements Analyzer<T, R> {

    private final Logger logger;
    private final int level;

    public AbstractAnalyzer(Logger logger, int level) {
        this.logger = logger;
        this.level = level;
    }

    /**
     * Analyzes a given element and returns the result of the analysis.
     * @param toAnalyze The element to be analyzed.
     * @return The result of the analysis.
     */
    public abstract R call(T toAnalyze);

    public void logReport(Report report) {
        String indent;
        if (this.level > 0) indent = "|\t".repeat(this.level);
        else indent = " \t".repeat(this.level);
        if (report.isComplete()) {
            if (report.isOk()) this.logger.success(indent + report);
            else this.logger.error(indent + report);
        } else {
            this.logger.info(indent + report);
        }
    }
}
