package ch.unibe.inf.seg.gitanalyzer.analyze;

import ch.unibe.inf.seg.gitanalyzer.conflict.ConflictingMerge;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Analyzer for an [iterable of conflicting merges]{@link Iterable<ConflictingMerge>}. The analyzer simply analyzes each
 * conflicting merge and returns an array of the results.
 */
public class ConflictingMergesAnalyzer extends Analyzer<Iterable<ConflictingMerge>, ArrayList<JSONObject>> {
    private final ConflictingMergeAnalyzer subAnalyzer = new ConflictingMergeAnalyzer();

    /**
     * Analyzes an [iterable of conflicting files]{@link Iterable<ConflictingMerge>}. The analyzer simply analyzes each
     * conflicting merge and returns an array of the results.
     * @param conflictingMerges The conflicting merges to be analyzed.
     * @return An array of the results.
     */
    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingMerge> conflictingMerges) {
        ArrayList<JSONObject> results = new ArrayList<>();

        for (ConflictingMerge conflictingMerge: conflictingMerges) {
            results.add(this.subAnalyzer.analyze(conflictingMerge));
        }

        return results;
    }
}
