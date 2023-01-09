package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectIterator;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.AnalyzerLogger;
import ch.unibe.inf.seg.gitanalyzer.util.logger.OutputStreamLogger;

import java.io.IOException;

public class ProjectListAnalyzer extends Thread implements Analyzer<ProjectList, ProjectListReport> {

    private AnalyzerLogger logger = new OutputStreamLogger(System.out, 0);
    private final ProjectAnalyzer subAnalyzer;

    public ProjectListAnalyzer() {
        this.subAnalyzer = new ProjectAnalyzer(this.logger);
    }

    public ProjectListAnalyzer(AnalyzerLogger logger) {
        this.logger = logger;
        this.subAnalyzer = new ProjectAnalyzer(this.logger);
    }

    @Override
    public ProjectListReport call(ProjectList projectList) {
        ProjectListReport report = new ProjectListReport(projectList.getListPath().toString());
        report.startTimer();

        // TODO: add other id than project_list
        this.logger.println(report, 0);

        try {
            ProjectInfos projectInfos = projectList.toProjectInfos();

            ProjectIterator projects = projectInfos.projectsIterator(projectList.getDirPathAbsolute());

            while (projects.hasNext()) {
                Project project = projects.next();
                report.addProjectReport(this.subAnalyzer.call(project));
                project.close();
            }

            report.ok();
        } catch (IOException e) {
            report.fail(e.getMessage());
        }

        report.endTimer();
        this.logger.println(report, 0);
        return report;
    }
}
