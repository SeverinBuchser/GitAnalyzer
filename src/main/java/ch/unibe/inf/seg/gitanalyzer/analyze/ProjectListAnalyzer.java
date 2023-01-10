package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectIterator;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.Logger;

import java.io.IOException;

public class ProjectListAnalyzer extends AbstractAnalyzer<ProjectList, ProjectListReport> {

    private final ProjectAnalyzer subAnalyzer;

    public ProjectListAnalyzer(Logger logger) {
        this(logger, 0);
    }

    public ProjectListAnalyzer(Logger logger, int level) {
        super(logger, level);
        this.subAnalyzer = new ProjectAnalyzer(logger, level + 1);
    }

    @Override
    public ProjectListReport call(ProjectList projectList) {
        ProjectListReport report = new ProjectListReport(projectList.getListPath().toString());
        report.startTimer();

        // TODO: add other id than project_list
        this.logReport(report);

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
        this.logReport(report);
        return report;
    }
}
