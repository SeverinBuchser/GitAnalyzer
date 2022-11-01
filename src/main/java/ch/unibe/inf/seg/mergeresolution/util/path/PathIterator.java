package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.*;

public class PathIterator<T> implements Iterator<Path<T>> {

    private final PathBuilder<T> builder;

    private int currentPathIndex = 0;

    public PathIterator(PathBuilder<T> builder) {
        this.builder = builder;
    }

    @Override
    public boolean hasNext() {
        return this.currentPathIndex < this.builder.getPathCount();
    }

    @Override
    public Path<T> next() {
        return new Path<>(this.currentPathIndex++, this.builder);
    }
}