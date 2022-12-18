package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.Collection;
import java.util.Iterator;

/**
 * An intersection consists out of a variable length path with a switch. The path before the intersection is
 * fixed but the intersection can point in various directions. All the possible directions are given by an
 * {@link Iterable} from which the number of elements, i.e. the size, is known. One can generate different paths from an
 * intersection. The different paths can be obtained by changing directions of the intersection. If the direction is
 * changed, there will be an indication to what happened when the direction changed. The indication can either be a
 * {@link IntersectionChange#CHANGE} or a {@link IntersectionChange#RESET}. The {@link IntersectionChange#RESET} will occur if the intersection has
 * no next direction, and it must be reset to the first direction.
 * @param <T> The type of the elements of the path.
 */
public class Intersection<T> implements IIntersection<T> {
    private final Path<T> path;
    private final Iterable<T> iterable;
    private Iterator<T> iterator;
    private final double size;

    private T tick;

    Intersection(Collection<T> collection) {
        this(new Path<>(), collection);
    }

    Intersection(Path<T> path, Collection<T> collection) {
        this(path, collection, collection.size());
    }

    Intersection(Path<T> path, SizeableIterable<T> iterable) {
        this(path, iterable, iterable.size());
    }

    private Intersection(Path<T> path, Iterable<T> iterable, double size) {
        this.path = path;
        this.iterable = iterable;
        this.iterator = this.iterable.iterator();
        this.size = size;
        this.changeDirection();
    }

    /**
     * {@inheritDoc}
     */
    public Path<T> getPath() {
        if (this.tick == null) return this.path;
        return this.path.append(this.tick);
    }

    /**
     * Changes direction of the intersection.
     * @return {@link IntersectionChange#CHANGE} if there is a next direction or {@link IntersectionChange#RESET} if there is no next
     * direction.
     */
    public IntersectionChange changeDirection() {
        if (this.iterator.hasNext()) {
            this.tick = this.iterator.next();
            return IntersectionChange.CHANGE;
        } else {
            this.iterator = this.iterable.iterator();
            if (this.iterator.hasNext()) {
                this.tick = this.iterator.next();
            }
            return IntersectionChange.RESET;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double size() {
        return this.size;
    }
}
