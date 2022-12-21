package ch.unibe.inf.seg.gitanalyzer.analyze;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract implementation of an analyzer, providing some widely used functionality for summation or counting of
 * elements.
 * @param <T> The type to be analyzed.
 * @param <R> The return type of the analysis.
 */
public abstract class Analyzer<T, R> {

    public static boolean verbose = false;

    /**
     * Analyzes a given element and returns the result of the analysis.
     * @param toAnalyze The element to be analyzed.
     * @return The result of the analysis.
     */
    abstract R analyze(T toAnalyze) throws IOException;

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

    protected static void printAnalyzing(String what, int indent) {
        printAnalyzing(what, indent, verbose);
    }

    protected static void printAnalyzing(String what, int indent, boolean verbose) {
        if (verbose) System.out.format("\t".repeat(indent) + "Analyzing %s:\n", what);
    }

    protected static void printComplete(String level, int indent, JSONObject result) {
        ResultState state = (ResultState) result.get("state");
        printComplete(
                level,
                indent,
                state,
                result.has("correct") && result.getBoolean("correct"),
                result.has("reason") ? result.getString("reason") : ""
        );
    }

    protected static void printComplete(String level, int indent, JSONObject result, boolean verbose) {
        ResultState state = (ResultState) result.get("state");
        printComplete(
                level,
                indent,
                state,
                result.has("correct") && result.getBoolean("correct"),
                result.has("reason") ? result.getString("reason") : "",
                verbose
        );
    }

    protected static void printComplete(String level, int indent, ResultState state, boolean correct) {
        printComplete(level, indent, state, correct, "");
    }

    protected static void printComplete(String level, int indent, ResultState state, boolean correct, String reason) {
        printComplete(level, indent, state, correct, reason, verbose);
    }

    protected static void printComplete(String level, int indent, ResultState state, boolean correct, String reason, boolean verbose) {
        if (verbose) {
            if (state == ResultState.OK) {
                System.out.format("\t".repeat(indent) + "%s: %5s: %b\n", level, state, correct);
            } else {
                System.out.format("\t".repeat(indent) + "%s: %5s: %s\n", level, state, reason);
            }
        }
    }
}
