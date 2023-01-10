package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

public class ConflictingFileReport extends MarkableReport {

    private final SubReports chunkReports = new SubReports();
    private final String filename;

    public ConflictingFileReport(String filename) {
        super("cf");
        this.filename = filename;
    }

    public void addChunkReport(ConflictingChunkReport chunkReport) {
        this.chunkReports.add(chunkReport);
    }

    @Override
    public JSONObject report() {
        JSONObject reportObject = super.report();
        reportObject.put("file_name", this.filename);
        if (this.isOk()) reportObject.put("ccs", this.chunkReports.report());
        return reportObject;
    }

    @Override
    public String toString() {
        if (!this.isComplete()) return String.format("Ongoing:  %s, %s", this.id, this.filename);
        return super.toString();
    }
}
