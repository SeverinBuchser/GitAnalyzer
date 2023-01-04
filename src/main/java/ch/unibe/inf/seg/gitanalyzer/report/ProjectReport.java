package ch.unibe.inf.seg.gitanalyzer.report;

import org.json.JSONObject;

public class ProjectReport extends Report {
    private final SubReports mergeReports = new SubReports();
    private final String projectName;
    private int commitCount = 0;
    private int mergeCount = 0;
    private int octopusMergeCount = 0;
    private int tagCount = 0;
    private int contributorCount = 0;


    public ProjectReport(String projectName) {
        super("project");
        this.projectName = projectName;
    }

    public void addMergeReport(ConflictingMergeReport mergeReport) {
        this.mergeReports.add(mergeReport);
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public void setMergeCount(int mergeCount) {
        this.mergeCount = mergeCount;
    }

    public void setOctopusMergeCount(int octopusMergeCount) {
        this.octopusMergeCount = octopusMergeCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public void setContributorCount(int contributorCount) {
        this.contributorCount = contributorCount;
    }

    @Override
    public JSONObject report() {
        JSONObject reportObject = super.report();
        reportObject.put("project_name", this.projectName);
        if (this.isOk()) {
            reportObject.put("cm", this.mergeReports.report());
            reportObject.put("commit_count", this.commitCount);
            reportObject.put("merge_count", this.mergeCount);
            reportObject.put("octopus_merge_count", this.octopusMergeCount);
            reportObject.put("tag_count", this.tagCount);
            reportObject.put("contributor_count", this.contributorCount);
        }
        return reportObject;
    }

    @Override
    public String toString() {
        if (!this.isComplete()) return String.format("In Progress: %s, %s", this.id, this.projectName);
        return super.toString();
    }
}
