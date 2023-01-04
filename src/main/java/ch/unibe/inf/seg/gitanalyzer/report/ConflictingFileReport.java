package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

import java.util.ArrayList;

public class ConflictingFileReport extends MarkableReport {

    private final ArrayList<Boolean> chunkReports = new ArrayList<>();
    private final String filename;

    public ConflictingFileReport(String filename) {
        super("cf");
        this.filename = filename;
    }

    public void addChunkReports(ArrayList<Boolean> chunkReports) {
        this.chunkReports.addAll(chunkReports);
    }

    @Override
    public JSONObject report() {
        JSONObject reportObject = super.report();
        reportObject.put("file_name", this.filename);
        if (this.isOk()) reportObject.put("cc", this.chunkReports);
        return reportObject;
    }

    @Override
    public String toString() {
        if (!this.isComplete()) return String.format("In Progress: %s, %s", this.id, this.filename);
        return super.toString();
    }
}
