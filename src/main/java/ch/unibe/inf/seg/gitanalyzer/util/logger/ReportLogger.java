package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.report.Report;

public interface ReportLogger {
    void report(Report report, int level);
}
