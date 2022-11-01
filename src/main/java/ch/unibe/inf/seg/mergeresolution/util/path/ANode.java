package ch.unibe.inf.seg.mergeresolution.util.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ANode<S extends ANode<S>> {

    public ArrayList<S> getChildren() {
        return children;
    }

    protected final ArrayList<S> children = new ArrayList<>();

    @SafeVarargs
    public final void addChildren(S... nodes) {
        this.addChildren(Arrays.stream(nodes).toList());
    }

    public void addChildren(List<S> nodes) {
        this.children.addAll(nodes);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }
}
