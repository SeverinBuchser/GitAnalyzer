package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

public class ConflictingMergeReport extends MarkableReport {

    private final SubReports fileReports = new SubReports();
    private final String commitId;

    public ConflictingMergeReport(String commitId) {
        super("cm");
        this.commitId = commitId;
    }

    public void addFileReport(ConflictingFileReport fileReport) {
        this.fileReports.add(fileReport);
    }

    @Override
    public JSONObject report() {
        JSONObject reportObject = super.report();
        reportObject.put("commit_id", this.commitId);
        if (this.isOk()) reportObject.put("cf", this.fileReports.report());
        return reportObject;
    }

    @Override
    public String toString() {
        if (!this.isComplete()) return String.format("In Progress: %s, %s", this.id, this.commitId);
        return super.toString();
    }
}
