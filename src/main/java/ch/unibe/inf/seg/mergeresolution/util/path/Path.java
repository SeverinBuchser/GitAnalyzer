package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.*;

/**
 * A collection of elements, which are ordered. A path can append another path, and it can also append an item. The
 * append method will not add to the existing path, but it will generate a new path, and leave the old one untouched.
 * @param <T> Type of elements of the path.
 */
public class Path<T> extends ArrayList<T> {

    /**
     * Initiates an empty path.
     */
    public Path() {}

    /**
     * Initiates a new path containing the supplied items in order.
     * @param items The items of the path.
     */
    public Path(List<T> items) {
        this.addAll(items);
    }

    /**
     * Clones this path and appends multiple items from a list to the cloned path.
     * @param path The items to append.
     * @return A clone of this path with the appended items.
     */
    public Path<T> append(List<T> path) {
        Path<T> newPath = new Path<>(this);
        newPath.addAll(path);
        return newPath;
    }

    /**
     * Clones this path and appends an item to the cloned path.
     * @param item The item to append.
     * @return A clone of this path with the appended new item.
     */
    public Path<T> append(T item) {
        Path<T> newPath = new Path<>(this);
        newPath.add(item);
        return newPath;
    }
}
