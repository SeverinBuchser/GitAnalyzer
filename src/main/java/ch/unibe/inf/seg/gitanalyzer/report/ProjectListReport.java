package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ProjectListReport extends Report {

    private final SubReports projectReports = new SubReports();
    private final String list;

    private Timestamp start;
    private Timestamp end;

    public ProjectListReport(String list) {
        super("project_list");
        this.list = list;
    }

    public void addProjectReport(ProjectReport projectReport) {
        this.projectReports.add(projectReport);
    }

    public void startTimer() {
        if (this.start != null) throw new IllegalStateException("The timer can only be used once.");
        this.start = new Timestamp(System.currentTimeMillis());
    }

    public void endTimer() {
        if (this.start == null) throw new IllegalStateException("The timer has not been started yet.");
        this.end = new Timestamp(System.currentTimeMillis());
    }

    public long duration() {
        if (this.end == null) throw new IllegalStateException("The timer has either not been started or stopped yet!");
        return this.end.getTime() - this.start.getTime();
    }

    public String formatDuration() {
        long duration = this.duration();
        long hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS);
        duration = duration - TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);

        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        duration = duration - TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);

        long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS);
        duration = duration - TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS);

        long milliseconds = TimeUnit.MILLISECONDS.convert(duration, TimeUnit.MILLISECONDS);

        return String.format("%2s:%2s:%2s:%3s", hours, minutes, seconds, milliseconds).replace(' ', '0');
    }

    @Override
    public JSONObject report() {
        JSONObject reportObject = super.report();
        reportObject.put("list", this.list);
        reportObject.put("start", this.start.toString());
        reportObject.put("end", this.end.toString());
        reportObject.put("duration", this.formatDuration());
        if (this.isOk()) reportObject.put("projects", this.projectReports.report());
        return reportObject;
    }

    @Override
    public String toString() {
        if (!this.isComplete()) return String.format("In Progress: %s, %s", this.id, this.list);
        return super.toString();
    }
}
