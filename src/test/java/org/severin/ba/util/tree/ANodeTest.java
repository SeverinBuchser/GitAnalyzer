package org.severin.ba.util.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class ANodeTest {

    private static class SimpleNode extends ANode<SimpleNode> {

        @Override
        public final void add(SimpleNode... nodes) {
            this.addList(Arrays.stream(nodes).toList());
        }

        @Override
        public void addList(List<SimpleNode> nodes) {
            this.children.addAll(nodes);
        }

        @Override
        public ArrayList<Stack<SimpleNode>> getPaths() {
            return null;
        }
    }

    @Test
    void isLeaf() {
        SimpleNode node1 = new SimpleNode();
        SimpleNode node2 = new SimpleNode();

        assertTrue(node1.isLeaf());

        node1.add(node2);

        assertFalse(node1.isLeaf());
        assertTrue(node2.isLeaf());
    }
}