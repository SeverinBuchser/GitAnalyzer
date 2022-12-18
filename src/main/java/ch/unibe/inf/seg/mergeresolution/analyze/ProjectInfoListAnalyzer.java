package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import ch.unibe.inf.seg.mergeresolution.project.ProjectsInfoListReader;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectInfoListAnalyzer extends Analyzer<ProjectsInfoListReader, JSONObject> {
    private final String projectDir;
    private final ProjectsAnalyzer subAnalyzer = new ProjectsAnalyzer();

    public ProjectInfoListAnalyzer(String projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public JSONObject analyze(ProjectsInfoListReader reader) {
        JSONObject result = new JSONObject();
        result.put("project_list", reader.name);

        Iterable<Project> projectIterable = reader.toProjects(this.projectDir);
        ArrayList<JSONObject> projects = this.subAnalyzer.analyze(projectIterable);

        result.put("state", ResultState.OK);
        result.put("projects", projects);
        result.put("projects_count", projects.size());

        putCount(result, projects, "projects_correct_count");
        putSum(result, projects, "conflicting_merges_count");
        putSum(result, projects, "conflicting_merges_correct_count");
        putSum(result, projects, "conflicting_files_count");
        putSum(result, projects, "conflicting_files_correct_count");
        putSum(result, projects, "conflicting_chunks_count");
        putSum(result, projects, "conflicting_chunks_correct_count");
        putSum(result, projects, "conflict_count");
        putSum(result, projects, "commits_count");
        putSum(result, projects, "merges_count");
        putSum(result, projects, "tags_count");
        putSum(result, projects, "contributors_count");

        return result;
    }
}
