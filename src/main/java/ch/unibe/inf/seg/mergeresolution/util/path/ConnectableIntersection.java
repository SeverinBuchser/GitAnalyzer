package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.Collection;

/**
 * Intersection with a connection to the next intersection. The intersection will also generate paths, but the
 * creation is different. If there is no next intersection, the path will be generated in the same way as in
 * {@link Intersection}. If there is an intersection, the path of the next intersection will be appended to the path of
 * this intersection. The direction also changes in a different way, such that every path of all the following
 * intersections will be reached. If there is no next intersection, the direction changes the same way as in
 * {@link Intersection}. If there is a next intersection, the direction will only change if the next intersection
 * resets its direction. Otherwise, there will be no change in direction of this intersection.
 * @param <T>
 */
public class ConnectableIntersection<T> extends Intersection<T> implements IConnectableIntersection<T> {
    private IConnectableIntersection<T> nextIntersection;

    ConnectableIntersection(Collection<T> collection) {
        super(new Path<>(), collection);
    }

    public ConnectableIntersection(SizeableIterable<T> iterable) {
        super(new Path<>(), iterable);
    }

    public ConnectableIntersection(Path<T> path, Collection<T> collection) {
        super(path, collection);
    }

    /**
     * {@inheritDoc}
     */
    public void connect(IConnectableIntersection<T> intersection) {
        this.nextIntersection = intersection;
    }

    /**
     * Creates the path of this intersection and appends the path of the next intersection if there is one.
     * @return The generated path.
     */
    @Override
    public Path<T> getPath() {
        if (this.hasNextIntersection()) {
            return super.getPath().append(this.nextIntersection.getPath());
        }
        return super.getPath();
    }

    /**
     * Changes direction in the same way as {@link Intersection}, if there is no next intersection. If there is a next
     * intersection, the direction of this intersection will only change if the next intersection has reset its
     * direction. Otherwise, there will be no change in direction.
     * @return {@link IntersectionChange#CHANGE} if there is no next intersection and there is a next direction or if
     * there is a next intersection, and it has reset its direction, {@link IntersectionChange#NO_CHANGE} if there is a
     * next intersection and the next intersection has not reset. {@link IntersectionChange#RESET} will be returned if
     * the next intersection has reset its direction and this intersection has no next direction.
     */
    @Override
    public IntersectionChange changeDirection() {
        if (this.hasNextIntersection()) {
            if (this.nextIntersection.changeDirection() == IntersectionChange.RESET) {
                return super.changeDirection();
            } else return IntersectionChange.NO_CHANGE;
        } else {
            return super.changeDirection();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNextIntersection() {
        return this.nextIntersection != null;
    }

    /**
     * {@inheritDoc}
     */
    public IConnectableIntersection<T> next() {
        return this.nextIntersection;
    }
}
