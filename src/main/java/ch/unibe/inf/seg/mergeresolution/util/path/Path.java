package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.*;

/**
 * A collection of elements, which are ordered. A path can append another path, and it can also append an item. The
 * append method will not add to the existing path, but it will generate a new path, and leave the old one untouched.
 * @param <T> Type of elements of the path.
 */
public class Path<T> extends ArrayList<T> {

    public Path() {}
    public Path(List<T> items) {
        this.addAll(items);
    }

    public Path<T> append(List<T> path) {
        Path<T> newPath = new Path<>(this);
        newPath.addAll(path);
        return newPath;
    }

    public Path<T> append(T item) {
        Path<T> newPath = new Path<>(this);
        newPath.add(item);
        return newPath;
    }
}
