package org.severin.ba.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class ANode<S extends ANode<S>> {

    protected final ArrayList<S> children = new ArrayList<>();

    public abstract void add(S... nodes);

    public abstract void addList(List<S> nodes);

    public abstract ArrayList<Stack<S>> getPaths();

    public boolean isLeaf() {
        return this.children.size() == 0;
    }
}
