package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.project.Project;
import ch.unibe.inf.seg.gitanalyzer.util.progress.Progressbar;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Analyzer for an [iterable of projects]{@link Iterable<Project>}. The analyzer simply analyzes each project and
 * returns an array of the results.
 */
public class ProjectsAnalyzer extends Analyzer<List<Project>, ArrayList<JSONObject>> {
    private final ProjectAnalyzer subAnalyzer = new ProjectAnalyzer();

    /**
     * Analyzes a [list of projects]{@link List<Project>}. The analyzer simply analyzes each project and returns
     * an array of the results.
     * @param projects The projects to be analyzed.
     * @return An array of the results.
     */
    @Override
    public ArrayList<JSONObject> analyze(List<Project> projects) {
        ArrayList<JSONObject> results = new ArrayList<>();

        if (verbose) {
            for (Project project: projects) {
                results.add(this.subAnalyzer.analyze(project));
                project.close();
            }
        } else {
            Progressbar bar = new Progressbar(System.out, projects.size(), 50);
            for (Project project: projects) {
                bar.setCurrent("Analyzing: " + project.name);
                results.add(this.subAnalyzer.analyze(project));
                try {
                    bar.step();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                project.close();
            }
        }

        return results;
    }
}
