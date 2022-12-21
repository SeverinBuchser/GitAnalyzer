package ch.unibe.inf.seg.gitanalyzer.util.path;

import java.util.Iterator;

/**
 * Iterator for iterating over all possible paths of {@link Intersections}.
 * @param <T> Type of elements of the paths.
 */
public class IntersectionsIterator<T> implements Iterator<Path<T>> {
    private final Intersections<T> intersections;
    private IntersectionChange latestChangeOfFirstIntersection;
    private boolean pathOfLatestChangeRetrieved = false;

    public IntersectionsIterator(Intersections<T> intersections) {
        this.intersections = intersections;
    }

    /**
     * Checks whether there is a next path. There will be a next path, if the most recent change of the first
     * intersection of the intersections is not a {@link IntersectionChange#RESET}. If there is a
     * {@link IntersectionChange#RESET}, the iterator is finished.
     * @return True if the iterator has a next path, false otherwise.
     */
    @Override
    public boolean hasNext() {
        if (!this.intersections.hasNextIntersection()) return false;
        if (this.pathOfLatestChangeRetrieved) {
            this.pathOfLatestChangeRetrieved = false;
            this.latestChangeOfFirstIntersection = this.intersections.changeDirection();
        }
        return this.latestChangeOfFirstIntersection != IntersectionChange.RESET;
    }

    /**
     * The next path of the intersections.
     * @return The next path.
     */
    @Override
    public Path<T> next() {
        if (this.hasNext()) {
            this.pathOfLatestChangeRetrieved = true;
            return this.intersections.getPath();
        }
        else return null;
    }
}
