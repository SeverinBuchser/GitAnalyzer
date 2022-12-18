package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Analyzer for an [iterable of projects]{@link Iterable<Project>}. The analyzer simply analyzes each project and
 * returns an array of the results.
 */
public class ProjectsAnalyzer extends Analyzer<Iterable<Project>, ArrayList<JSONObject>> {
    private final ProjectAnalyzer subAnalyzer = new ProjectAnalyzer();

    /**
     * Analyzes an [iterable of project]{@link Iterable<Project>}. The analyzer simply analyzes each project and returns
     * an array of the results.
     * @param projects The projects to be analyzed.
     * @return An array of the results.
     */
    @Override
    public ArrayList<JSONObject> analyze(Iterable<Project> projects) {
        ArrayList<JSONObject> results = new ArrayList<>();

        for (Project project: projects) {
            results.add(this.subAnalyzer.analyze(project));
            project.close();
        }

        return results;
    }
}
