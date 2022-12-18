package ch.unibe.inf.seg.mergeresolution.util.path;

/**
 * {@link Intersection} with a connection to the next {@link IConnectableIntersection}.
 * @param <T> The type of the elements of the {@link Path} of the intersection.
 */
public interface IConnectableIntersection<T> extends IIntersection<T> {
    /**
     * Set the next intersection.
     * @param connectable Next {@link IConnectableIntersection}
     */
    void connect(IConnectableIntersection<T> connectable);

    /**
     * Check if there is a next intersection.
     * @return True if there is a next intersection and false otherwise.
     */
    boolean hasNextIntersection();

    /**
     * Returns the next intersection.
     * @return Next {@link IConnectableIntersection}
     */
    IConnectableIntersection<T> next();
}
