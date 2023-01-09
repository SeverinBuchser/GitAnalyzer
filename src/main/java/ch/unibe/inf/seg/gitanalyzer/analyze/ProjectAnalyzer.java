package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.AnalyzerLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.OutputStreamLogger;

public class ProjectAnalyzer implements Analyzer<Project, ProjectReport> {

    private AnalyzerLogger logger = new OutputStreamLogger(System.out, 1);
    private final ConflictingMergeAnalyzer subAnalyzer;

    public ProjectAnalyzer() {
        this.subAnalyzer = new ConflictingMergeAnalyzer(this.logger);
    }

    public ProjectAnalyzer(AnalyzerLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ConflictingMergeAnalyzer(this.logger);
    }

    @Override
    public ProjectReport call(Project project) {
        ProjectReport report = new ProjectReport(project.getName());
        this.logger.println(report, 1);

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

        this.logger.println(report, 1);
        return report;
    }
}
