package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.project.Project;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Analyzer for a {@link Project}.
 * For each conflicting merge of the project, the {@link ConflictingMergesAnalyzer} is run. For each project some
 * additional data will be stored and returned.
 */
public class ProjectAnalyzer extends Analyzer<Project, JSONObject> {
    private final ConflictingMergesAnalyzer subAnalyzer = new ConflictingMergesAnalyzer();

    /**
     * Analyzes a {@link Project}.
     * For each conflicting merge of the project, the {@link ConflictingMergesAnalyzer} is run. Each result will be
     * added to an array which will be returned under the key "conflicting_merges". For each project additional data is
     * returned.
     * @param project The project to be analyzed.
     * @return The results of the analyzed project.
     */
    @Override
    public JSONObject analyze(Project project) {
        JSONObject result = new JSONObject();
        result.put("project_name", project.name);
        boolean correct = false;
        printAnalyzing(project);

        try {
            ArrayList<JSONObject> merges = this.subAnalyzer.analyze(project);

            result.put("state", ResultState.OK);
            result.put("conflicting_merges", merges);
            result.put("conflicting_merges_count", merges.size());
            putCount(result, merges, "conflicting_merges_correct_count");
            putSum(result, merges, "conflicting_files_count");
            putSum(result, merges, "conflicting_files_correct_count");
            putSum(result, merges, "conflicting_chunks_count");
            putSum(result, merges, "conflicting_chunks_correct_count");

            correct = result.getInt("conflicting_merges_correct_count") == result.getInt("conflicting_merges_count")
                    && result.getInt("conflicting_merges_correct_count") != 0;
            result.put("correct", correct);
            result.put("conflict_count", merges.stream().map(x -> x.getInt("conflict_count")).reduce(0, Integer::sum));

            // metadata
            result.put("commits_count", project.getCommitsCount());
            result.put("merges_count", project.getMergesCount());
            result.put("tags_count", project.getTagsCount());
            result.put("contributors_count", project.getCommitsCount() > 0 ? project.getContributorsCount() : 0);
        } catch (Exception e) {
            result.put("state", ResultState.FAIL);
            result.put("reason", e.getMessage());
        }

        System.out.format("Project: %5s: %b\n", result.get("state"), correct);
        return result;
    }

    private static void printAnalyzing(Project project) {
        System.out.format("Analyzing %s:\n", project.name);
    }
}
