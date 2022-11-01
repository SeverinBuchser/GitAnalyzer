package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.*;

public class PathBuilder<T> extends ANode<Node<T>> implements Iterable<Path<T>> {

    private ArrayList<Node<T>> heads;

    private final ArrayList<Integer> layerSizes = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> buildPathSignatures() {
        ArrayList<ArrayList<Integer>> pathSignatures = new ArrayList<>();
        if (this.layerSizes.size() == 0) return pathSignatures;
        pathSignatures.add(new ArrayList<>());

        for (Integer layerSize: this.layerSizes) {
            ArrayList<ArrayList<Integer>> oldSignatures = pathSignatures;
            pathSignatures = new ArrayList<>();

            for (ArrayList<Integer> oldSignature: oldSignatures) {
                for (int i = 0 ; i < layerSize ; i++) {
                    ArrayList<Integer> newSignature = new ArrayList<>(oldSignature);
                    newSignature.add(i);
                    pathSignatures.add(newSignature);
                }
            }
        }
        return pathSignatures;
    }

    @SafeVarargs
    public final void addLayer(Node<T>... nodes) {
        List<Node<T>> nodeList = Arrays.stream(nodes).toList();
        this.addLayer(nodeList);
    }

    public void addLayer(List<Node<T>> nodes) {
        if (this.heads == null) {
            this.children.addAll(nodes);
        } else {
            for (Node<T> head: this.heads) {
                head.addChildren(nodes);
            }
        }
        this.layerSizes.add(nodes.size());
        this.heads = new ArrayList<>(nodes);
    }

    @SafeVarargs
    public final void addItemLayer(T... items) {
        List<T> itemList = Arrays.stream(items).toList();
        this.addItemLayer(itemList);
    }

    public void addItemLayer(List<T> items) {
        ArrayList<Node<T>> nodes = new ArrayList<>();
        items.forEach(item -> nodes.add(new Node<>(item)));
        this.addLayer(nodes);
    }

    public int getPathCount() {
        if (this.layerSizes.size() == 0) return 0;
        return this.layerSizes.stream().reduce(1, (count, layerSize) -> count * layerSize);
    }

    @Override
    public Iterator<Path<T>> iterator() {
        return new PathIterator<>(this);
    }
}
