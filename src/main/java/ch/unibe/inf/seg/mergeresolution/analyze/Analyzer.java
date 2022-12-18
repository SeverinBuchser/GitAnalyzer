package ch.unibe.inf.seg.mergeresolution.analyze;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Abstract implementation of an analyzer, providing some widely used functionality for summation or counting of
 * elements.
 * @param <T> The type to be analyzed.
 * @param <R> The return type of the analysis.
 */
public abstract class Analyzer<T, R> {

    /**
     * Analyzes a given element and returns the result of the analysis.
     * @param toAnalyze The element to be analyzed.
     * @return The result of the analysis.
     */
    abstract R analyze(T toAnalyze);

    protected static void putSum(JSONObject result, ArrayList<JSONObject> list, String key) {
        result.put(key, list.stream().map(x -> {
            if (x.get("state") == ResultState.OK) return x.getInt(key);
            else return 0;
        }).reduce(0, Integer::sum));
    }

    protected static void putCount(JSONObject result, ArrayList<JSONObject> list, String key) {
        result.put(key, list.stream().map(x -> {
            if (x.get("state") == ResultState.OK) return x.getBoolean("correct");
            else return false;
        }).filter(c -> c).count());
    }
}
