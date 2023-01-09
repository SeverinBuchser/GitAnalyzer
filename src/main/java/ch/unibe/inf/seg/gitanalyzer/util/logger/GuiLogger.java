package ch.unibe.inf.seg.gitanalyzer.util.logger;

import ch.unibe.inf.seg.gitanalyzer.report.Report;

import javax.swing.*;

public class GuiLogger extends JTextPane implements ReportLogger {
    @Override
    public void report(Report report, int level) {
        this.setText(report.toString(level));
    }
}
