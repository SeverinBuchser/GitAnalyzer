package org.severin.ba.util.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Node<T> extends ANode<Node<T>> {

    public T getItem() {
        return item;
    }

    private final T item;

    public Node(T item) {
        this.item = item;
    }

    @SafeVarargs
    @Override
    public final void add(Node<T>... nodes) {
        this.addList(Arrays.stream(nodes).toList());
    }

    @Override
    public void addList(List<Node<T>> nodes) {
        this.children.addAll(nodes);
    }

    @Override
    public ArrayList<Stack<Node<T>>> getPaths() {
        ArrayList<Stack<Node<T>>> paths = new ArrayList<>();

        if (this.isLeaf()) {
            Stack<Node<T>> path = new Stack<>();
            path.push(this);
            paths.add(path);
            return paths;
        }

        for (Node<T> child: children) {
            ArrayList<Stack<Node<T>>> childPaths = child.getPaths();
            for (Stack<Node<T>> path: childPaths) {
                path.push(this);
                paths.add(path);
            }
        }

        return paths;
    }
}
