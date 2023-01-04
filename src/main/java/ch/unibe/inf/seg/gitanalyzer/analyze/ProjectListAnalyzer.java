package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectInfos;
import ch.unibe.inf.seg.gitanalyzer.project.ProjectIterator;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;

import java.io.IOException;

public class ProjectListAnalyzer implements Analyzer<ProjectList, ProjectListReport> {
    private final ProjectAnalyzer subAnalyzer = new ProjectAnalyzer();

    @Override
    public ProjectListReport analyze(ProjectList projectList) throws IOException {
        ProjectListReport report = new ProjectListReport(projectList.getList());
        report.startTimer();

        // TODO: add other id than project_list
        System.out.println(report.toString(0));

        ProjectInfos projectInfos = projectList.toProjectInfos();

        ProjectIterator projects = projectInfos.projectsIterator(projectList.getDirRelative());

        while (projects.hasNext()) {
            Project project = projects.next();
            report.addProjectReport(this.subAnalyzer.analyze(project));
            project.close();
        }

        report.ok();
        report.endTimer();

        System.out.println(report.toString(0));
        return report;
    }
}
