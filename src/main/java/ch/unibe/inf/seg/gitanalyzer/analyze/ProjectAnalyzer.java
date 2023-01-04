package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectReport;

public class ProjectAnalyzer implements Analyzer<Project, ProjectReport> {
    private final ConflictingMergeAnalyzer subAnalyzer = new ConflictingMergeAnalyzer();

    @Override
    public ProjectReport analyze(Project project) {
        ProjectReport report = new ProjectReport(project.getName());
        System.out.println(report.toString(1));

        try {
            for (ConflictingMerge conflictingMerge: project) {
                report.addMergeReport(this.subAnalyzer.analyze(conflictingMerge));
            }

            report.ok();

            // metadata
            Integer[] counts = project.getCommitsMergesOctopusMergesCount();
            report.setCommitCount(counts[0]);
            report.setMergeCount(counts[1]);
            report.setOctopusMergeCount(counts[2]);
            report.setTagCount(project.getTagsCount());
            report.setContributorCount(counts[0] > 0 ? project.getContributorsCount() : 0);
        } catch (Exception e) {
            report.fail(e.getMessage());
        }

        System.out.println(report.toString(1));
        return report;
    }
}
