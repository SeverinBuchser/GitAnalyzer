package org.severin.ba.util.path;

import java.util.ArrayList;

public class Path<T> {
    private final PathBuilder<T> pathBuilder;
    private final int pathIndex;

    public Path(int pathIndex, PathBuilder<T> pathBuilder) {
        this.pathIndex = pathIndex;
        this.pathBuilder = pathBuilder;
    }

    public ArrayList<T> build() {
        ArrayList<ArrayList<Integer>> pathSignatures = this.pathBuilder.buildPathSignatures();

        if (pathIndex >= pathSignatures.size()) return new ArrayList<>();
        ArrayList<Integer> pathSignature = pathSignatures.get(pathIndex);
        ArrayList<T> path = new ArrayList<>();
        ANode<Node<T>> lastNode = this.pathBuilder;
        for (int childIndex: pathSignature) {
            Node<T> currentNode = lastNode.getChildren().get(childIndex);
            path.add(currentNode.getItem());
            lastNode =  currentNode;
        }
        return path;
    }
}
