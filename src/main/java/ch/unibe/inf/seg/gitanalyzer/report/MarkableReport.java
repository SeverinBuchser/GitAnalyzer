package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

public class MarkableReport extends Report {

    private boolean mark = false;
    public MarkableReport(String id) {
        super(id);
    }

    public boolean isMarked() {
        this.preconditionIsComplete();
        if (!this.isOk()) throw new IllegalStateException("The report is not " + ReportState.OK + ".");
        return this.mark;
    }

    @Override
    public void ok() {
        throw new UnsupportedOperationException("A markable report has to be marked.");
    }

    public void ok(boolean mark) {
        super.ok();
        this.mark = mark;
    }

    public JSONObject report() {
        JSONObject reportObject = super.report();
        if (this.isOk()) reportObject.put("mark", this.mark);
        return reportObject;
    }

    @Override
    public String toString() {
        if (this.isComplete() && this.isOk()) {
            return String.format("%s: %5s: %b", this.id, this.state, this.isMarked());
        }
        return super.toString();
    }
}
