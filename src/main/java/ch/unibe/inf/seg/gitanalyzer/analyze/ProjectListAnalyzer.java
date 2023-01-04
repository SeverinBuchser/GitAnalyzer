package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectIterator;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.AnalyzerLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.SimpleAnalyzerLogger;

import java.io.IOException;

public class ProjectListAnalyzer implements Analyzer<ProjectList, ProjectListReport> {

    private AnalyzerLogger logger = new SimpleAnalyzerLogger(System.out, 0);
    private final ProjectAnalyzer subAnalyzer;

    public ProjectListAnalyzer() {
        this.subAnalyzer = new ProjectAnalyzer(this.logger);
    }

    public ProjectListAnalyzer(AnalyzerLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ProjectAnalyzer(this.logger);
    }

    @Override
    public ProjectListReport analyze(ProjectList projectList) throws IOException {
        ProjectListReport report = new ProjectListReport(projectList.getList());
        report.startTimer();

        // TODO: add other id than project_list
        this.logger.println(report, 0);

        ProjectInfos projectInfos = projectList.toProjectInfos();

        ProjectIterator projects = projectInfos.projectsIterator(projectList.getDirRelative());

        while (projects.hasNext()) {
            Project project = projects.next();
            report.addProjectReport(this.subAnalyzer.analyze(project));
            project.close();
        }

        report.ok();
        report.endTimer();

        this.logger.println(report, 0);
        return report;
    }
}
