package ch.unibe.inf.seg.mergeresolution.analyze;

import ch.unibe.inf.seg.mergeresolution.conflict.ConflictingMerge;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConflictingMergesAnalyzer extends Analyzer<Iterable<ConflictingMerge>, ArrayList<JSONObject>> {
    private final ConflictingMergeAnalyzer subAnalyzer = new ConflictingMergeAnalyzer();
    @Override
    public ArrayList<JSONObject> analyze(Iterable<ConflictingMerge> conflictingMerges) {
        ArrayList<JSONObject> results = new ArrayList<>();

        for (ConflictingMerge conflictingMerge: conflictingMerges) {
            results.add(this.subAnalyzer.analyze(conflictingMerge));
        }

        return results;
    }
}
