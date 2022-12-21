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
        boolean correct;
        printAnalyzing(project.name, 1);

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
            result.put("all_conflicting_chunks_count", merges.stream().map(x -> x.getInt("all_conflicting_chunks_count")).reduce(0, Integer::sum));

            // metadata
            Integer[] counts = project.getCommitsMergesOctopusMergesCount();
            result.put("commits_count", counts[0]);
            result.put("merges_count", counts[1]);
            result.put("octopus_merges_count", counts[2]);
            result.put("tags_count", project.getTagsCount());
            result.put("contributors_count", counts[0] > 0 ? project.getContributorsCount() : 0);
        } catch (Exception e) {
            result.put("state", ResultState.FAIL);
            result.put("reason", e.getMessage());
        }

        printComplete("Project", 1, result);
        return result;
    }
}
