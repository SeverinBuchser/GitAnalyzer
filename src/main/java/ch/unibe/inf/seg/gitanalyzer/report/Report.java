package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

public class Report implements Reportable {

    protected final String id;
    protected ReportState state;
    protected String reason;

    public ReportState getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public Report(String id) {
        this.id = id;
    }

    public boolean isComplete() {
        return this.state != null;
    }

    protected void preconditionIsComplete() {
        if (!this.isComplete()) throw new IllegalStateException("The report has not been completed yet.");
    }

    public void ok() {
        this.state = ReportState.OK;
    }

    public void skip(String reason) {
        this.state = ReportState.SKIP;
        this.reason = reason;
    }

    public void fail(String reason) {
        this.state = ReportState.FAIL;
        this.reason = reason;
    }

    public boolean isOk() {
        this.preconditionIsComplete();
        return this.state == ReportState.OK;
    }

    public boolean isSkip() {
        this.preconditionIsComplete();
        return this.state == ReportState.SKIP;
    }

    public boolean isFail() {
        this.preconditionIsComplete();
        return this.state == ReportState.FAIL;
    }

    public JSONObject report() {
        JSONObject reportObject = new JSONObject();
        reportObject.put("id", this.id);
        reportObject.put("state", this.state);
        if (this.isSkip() || this.isFail()) reportObject.put("reason", this.reason);
        return reportObject;
    }

    @Override
    public String toString() {
        if (this.isComplete()) {
            if (this.isOk()) return String.format("%s: %5s", this.id, this.state);
            return String.format("%s: %5s, %s", this.id, this.state, this.reason);
        }
        return String.format("In Progress: %s", this.id);
    }

    public String toString(int indent) {
        return "\t".repeat(indent) + this;
    }

    protected enum ReportState {
        OK("OK"),
        SKIP("SKIP"),
        FAIL("FAIL");
        private final String stateString;

        ReportState(String stateString) {
            this.stateString = stateString;
        }

        @Override
        public String toString() {
            return this.stateString;
        }
    }
}
