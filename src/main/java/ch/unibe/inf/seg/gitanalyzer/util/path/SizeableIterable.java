package ch.unibe.inf.seg.gitanalyzer.util.path;

/**
 * Iterable with size.
 * @param <T> The type of the {@link Iterable}.
 */
public interface SizeableIterable<T> extends Iterable<T> {

    /**
     * The number of elements the iterable can iterate over.
     * @return The size of the iterator of the iterable.
     */
    double size();
}
