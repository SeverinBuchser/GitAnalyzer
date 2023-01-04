package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONArray;

import java.util.ArrayList;

public class SubReports implements Reportable {

    private final ArrayList<Report> subReports = new ArrayList<>();

    public void add(Report subReport) {
        if (!subReport.isComplete()) throw new IllegalStateException("The sub-report has to be completed.");
        this.subReports.add(subReport);
    }

    @Override
    public JSONArray report() {
        JSONArray reportArrayObject = new JSONArray();
        for (Report report: this.subReports) {
            reportArrayObject.put(report.report());
        }
        return reportArrayObject;
    }
}
