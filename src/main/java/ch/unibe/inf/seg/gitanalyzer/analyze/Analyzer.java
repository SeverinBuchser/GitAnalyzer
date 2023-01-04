package ch.unibe.inf.seg.gitanalyzer.analyze;

import java.io.IOException;

/**
 * Abstract implementation of an analyzer, providing some widely used functionality for summation or counting of
 * elements.
 * @param <T> The type to be analyzed.
 * @param <R> The return type of the analysis.
 */
public interface Analyzer<T, R> {

    /**
     * Analyzes a given element and returns the result of the analysis.
     * @param toAnalyze The element to be analyzed.
     * @return The result of the analysis.
     */
    R analyze(T toAnalyze) throws IOException;
}
