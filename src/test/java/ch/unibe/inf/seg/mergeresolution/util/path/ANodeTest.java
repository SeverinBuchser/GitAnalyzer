package ch.unibe.inf.seg.mergeresolution.util.path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ANodeTest {

    private static class SimpleNode extends ANode<SimpleNode> {}

    @Test
    void isLeaf() {
        SimpleNode node1 = new SimpleNode();
        SimpleNode node2 = new SimpleNode();

        assertTrue(node1.isLeaf());

        node1.addChildren(node2);

        assertFalse(node1.isLeaf());
        assertTrue(node2.isLeaf());
    }
}