package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.report.Report;

public interface AnalyzerLogger {
    void println(Report report, int level);
}
