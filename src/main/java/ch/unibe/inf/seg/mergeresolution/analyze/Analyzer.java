package ch.unibe.inf.seg.mergeresolution.analyze;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Analyzer<T, R> {
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
