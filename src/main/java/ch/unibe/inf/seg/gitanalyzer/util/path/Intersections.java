package ch.unibe.inf.seg.gitanalyzer.util.path;

/**
 * Represents a collection of intersections, which is iterable. The iteration is over all the possible paths of the
 * intersections. Since this class implements {@link IConnectableIntersection} and {@link Iterable<Path>}, it can be
 * viewed as an intersection and connected to other intersections. This means that one can create intersections of
 * intersections. This is usefull, because one gets a path of paths when iterating over the intersections and will
 * therefore have a natural separation of the paths of each intersection and not just one long path.
 * @param <T> Type of the elements of the path.
 */
public class Intersections<T> implements Iterable<Path<T>>, IConnectableIntersection<T> {
    private IConnectableIntersection<T> firstIntersection;
    private IConnectableIntersection<T> lastIntersection;

    /**
     * Adds an intersection to the intersections. The intersection to connect will be connected to the most recent
     * connected intersection.
     * @param intersection Next {@link IConnectableIntersection}
     */
    public void connect(IConnectableIntersection<T> intersection) {
        if (this.firstIntersection == null) this.firstIntersection = intersection;
        else this.lastIntersection.connect(intersection);
        this.lastIntersection = intersection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNextIntersection() {
        return this.firstIntersection != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConnectableIntersection<T> next() {
        return this.firstIntersection;
    }

    /**
     * {@inheritDoc}
     */
    public Path<T> getPath() {
        if (this.hasNextIntersection()) return this.firstIntersection.getPath();
        return new Path<>();
    }

    /**
     * {@inheritDoc}
     */
    public IntersectionChange changeDirection() {
        if (this.hasNextIntersection()) return this.firstIntersection.changeDirection();
        return null;
    }

    /**
     * Iterator for all possible paths of the intersections.
     * @return An iterator over all possible paths.
     */
    @Override
    public IntersectionsIterator<T> iterator() {
        return new IntersectionsIterator<>(this);
    }
}
