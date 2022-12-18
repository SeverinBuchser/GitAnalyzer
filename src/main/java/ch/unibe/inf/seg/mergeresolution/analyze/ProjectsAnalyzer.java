package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectsAnalyzer extends Analyzer<Iterable<Project>, ArrayList<JSONObject>> {
    private final ProjectAnalyzer subAnalyzer = new ProjectAnalyzer();
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
