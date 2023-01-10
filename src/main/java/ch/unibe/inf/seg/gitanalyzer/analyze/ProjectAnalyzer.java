package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

public class ProjectAnalyzer extends AbstractAnalyzer<Project, ProjectReport> {

    private final ConflictingMergeAnalyzer subAnalyzer;

    public ProjectAnalyzer(Logger logger) {
        this(logger, 0);
    }

    public ProjectAnalyzer(Logger logger, int level) {
        super(logger, level);
        this.subAnalyzer = new ConflictingMergeAnalyzer(logger, level + 1);
    }

    @Override
    public ProjectReport call(Project project) {
        ProjectReport report = new ProjectReport(project.getName());
        this.logReport(report);

        try {
            for (ConflictingMerge conflictingMerge: project) {
                report.addMergeReport(this.subAnalyzer.call(conflictingMerge));
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

        this.logReport(report);
        return report;
    }
}
