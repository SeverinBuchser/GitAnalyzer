package org.severin.ba.util.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void getItem() {
        Node<String> node = new Node<>("n1");
        assertEquals("n1", node.getItem());
    }

    @Test
    void add() {
        Node<String> node1 = new Node<>("n1");
        Node<String> node2 = new Node<>("n2");
        Node<String> node3 = new Node<>("n3");

        node1.add(node2, node3);
        ArrayList<Stack<Node<String>>> paths = node1.getPaths();
        assertEquals(2, paths.size());
        assertEquals(2, paths.get(0).size());
        assertEquals(2, paths.get(1).size());
    }

    @Test
    void addList() {
        List<Node<String>> nodeList = new ArrayList<>();
        Node<String> node1 = new Node<>("n1");
        Node<String> node2 = new Node<>("n2");
        Node<String> node3 = new Node<>("n3");

        nodeList.add(node2);
        nodeList.add(node3);
        node1.addList(nodeList);

        ArrayList<Stack<Node<String>>> paths = node1.getPaths();
        assertEquals(2, paths.size());
        assertEquals(2, paths.get(0).size());
        assertEquals(2, paths.get(1).size());
    }

    @Test
    void getPaths() {
        Node<String> node1 = new Node<>("n1");
        Node<String> node2 = new Node<>("n2");
        Node<String> node3 = new Node<>("n3");
        Node<String> node4 = new Node<>("n4");
        Node<String> node5 = new Node<>("n5");
        Node<String> node6 = new Node<>("n6");
        Node<String> node7 = new Node<>("n7");

        node1.add(node2, node3);
        node2.add(node4, node5);
        node3.add(node4, node6);
        node6.add(node7);

        /**
         *          n1
         *         /  \
         *       n2     n3
         *      /  \    |
         *   n4     n5   n6
         *               |
         *               n7
         *   and n3 is connected to n4
         *   Paths:
         *   * n1 n2 n4
         *   * n1 n2 n5
         *   * n1 n3 n4
         *   * n1 n3 n6 n7
         */

        ArrayList<Stack<Node<String>>> paths = node1.getPaths();

        assertEquals(4, paths.size());

        Stack<Node<String>> path1 = paths.get(0);
        assertEquals(node1, path1.pop());
        assertEquals(node2, path1.pop());
        assertEquals(node4, path1.pop());

        Stack<Node<String>> path2 = paths.get(1);
        assertEquals(node1, path2.pop());
        assertEquals(node2, path2.pop());
        assertEquals(node5, path2.pop());

        Stack<Node<String>> path3 = paths.get(2);
        assertEquals(node1, path3.pop());
        assertEquals(node3, path3.pop());
        assertEquals(node4, path3.pop());

        Stack<Node<String>> path4 = paths.get(3);
        assertEquals(node1, path4.pop());
        assertEquals(node3, path4.pop());
        assertEquals(node6, path4.pop());
        assertEquals(node7, path4.pop());
    }
}