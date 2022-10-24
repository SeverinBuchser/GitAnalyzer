package org.severin.ba.util.path;

public class Node<T> extends ANode<Node<T>> {

    public T getItem() {
        return item;
    }

    private final T item;

    public Node(T item) {
        this.item = item;
    }
}
