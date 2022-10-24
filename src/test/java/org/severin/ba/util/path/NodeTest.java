package org.severin.ba.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void getItem() {
        Node<String> node = new Node<>("n1");
        assertEquals("n1", node.getItem());
    }

    @Test
    void addChildrenTestVarargs() {
        Node<String> node1 = new Node<>("n1");
        Node<String> node2 = new Node<>("n2");
        Node<String> node3 = new Node<>("n3");

        node1.addChildren(node2, node3);
        ArrayList<Node<String>> children = node1.getChildren();
        assertEquals(2, children.size());
        assertEquals(node2, children.get(0));
        assertEquals(node3, children.get(1));
    }

    @Test
    void addChildrenTestList() {
        List<Node<String>> nodeList = new ArrayList<>();
        Node<String> node1 = new Node<>("n1");
        Node<String> node2 = new Node<>("n2");
        Node<String> node3 = new Node<>("n3");

        nodeList.add(node2);
        nodeList.add(node3);
        node1.addChildren(nodeList);

        ArrayList<Node<String>> children = node1.getChildren();
        assertEquals(2, children.size());
        assertEquals(node2, children.get(0));
        assertEquals(node3, children.get(1));
    }
}