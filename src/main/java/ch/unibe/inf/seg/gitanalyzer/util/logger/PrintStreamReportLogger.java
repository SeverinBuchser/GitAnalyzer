package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.report.Report;

public class PrintStreamReportLogger implements ReportLogger {

    private final Logger logger;
    private final int levelZero;

    public PrintStreamReportLogger(PrintStreamLogger out, int levelZero) {
        this.logger = out;
        this.levelZero = levelZero;
    }

    public void report(Report report, int level) {
        if (level < this.levelZero) throw new IllegalArgumentException("The level has to be bigger or equal than the " +
                "level zero set at construction.");
        level =- this.levelZero;
        if (report.isComplete()) {
            if (report.isOk()) this.logger.success(report.toString(level));
            else this.logger.error(report.toString(level));
        } else {
            this.logger.info(report.toString(level));
        }
    }
}
