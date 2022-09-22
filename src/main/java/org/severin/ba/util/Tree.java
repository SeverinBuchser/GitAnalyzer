package org.severin.ba.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Tree<T> extends ANode<Node<T>> {

    private ArrayList<Node<T>> heads;

    @SafeVarargs
    @Override
    public final void add(Node<T>... nodes) {
        List<Node<T>> nodeList = Arrays.stream(nodes).toList();
        this.addList(nodeList);
    }

    @Override
    public void addList(List<Node<T>> nodes) {
        if (this.heads == null) {
            this.children.addAll(nodes);
        } else {
            for (Node<T> head: this.heads) {
                head.addList(nodes);
            }
        }
        this.heads = new ArrayList<>(nodes);
    }

    @Override
    public ArrayList<Stack<Node<T>>> getPaths() {
        ArrayList<Stack<Node<T>>> paths = new ArrayList<>();

        for (Node<T> child: children) {
            ArrayList<Stack<Node<T>>> childPaths = child.getPaths();
            paths.addAll(childPaths);
        }

        return paths;
    }
}
