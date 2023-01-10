package ch.unibe.inf.seg.gitanalyzer.cli;

import ch.unibe.inf.seg.gitanalyzer.analyze.ProjectListAnalyzer;
import ch.unibe.inf.seg.gitanalyzer.config.ProjectList;
import ch.unibe.inf.seg.gitanalyzer.report.ProjectListReport;
import ch.unibe.inf.seg.gitanalyzer.util.logger.LoggerProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractAnalyzeCommand implements Runnable {

    protected abstract File getOutFile(ProjectList projectList);

    protected void analyzeProjectList(ProjectList projectList) {
        ProjectListAnalyzer analyzer = new ProjectListAnalyzer(LoggerProvider.getLogger());
        ProjectListReport report = analyzer.call(projectList);
        LoggerProvider.getLogger().info(
                2,
                String.format("Saving Analysis Report for Project List: %s", projectList.getListPath())
        );
        try {
            File outFile = this.getOutFile(projectList);
            FileWriter writer = new FileWriter(outFile);
            writer.write(report.report().toString(4));
            writer.close();
            LoggerProvider.getLogger().success(String.format("Saved Analysis Report: '%s'", outFile));
        } catch (IOException e) {
            LoggerProvider.getLogger().fail(e.getMessage());
        }
    }
}
