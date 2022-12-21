package ch.unibe.inf.seg.gitanalyzer.util.path;

/**
 * An intersection can generate a {@link Path}. The intersection can change directions to create a new {@link Path}. The
 * change in direction will return an indication in form of a {@link IntersectionChange} which indicates if the direction has
 * changed, has not changed or has reset. The intersection must also have a size, which is the number of directions.
 * @param <T> The type of elements of the {@link Path}.
 */
public interface IIntersection<T> {

    /**
     * The path of the current direction of the intersection.
     * @return The current path.
     */
    Path<T> getPath();

    /**
     * Changes, does not change or resets the direction of the intersection.
     * @return {@link IntersectionChange#CHANGE} if the direction has changed, {@link IntersectionChange#NO_CHANGE} if the direction has not
     * changed or {@link IntersectionChange#RESET} if the direction has reset to the first direction.
     */
    IntersectionChange changeDirection();

    /**
     * The number of directions of the intersection.
     * @return The number of directions.
     */
    double size();
}
