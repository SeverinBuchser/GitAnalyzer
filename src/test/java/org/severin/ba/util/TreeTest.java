package org.severin.ba.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    @Test
    void treeConstruction() {
        Tree<String> tree = new Tree<>();
        Node<String> node11 = new Node<>("11");
        tree.add(node11);


        assertTrue(node11.isLeaf());


        Node<String> node21 = new Node<>("21");
        Node<String> node22 = new Node<>("22");
        tree.add(node21, node22);


        assertFalse(node11.isLeaf());
        assertTrue(node21.isLeaf());
        assertTrue(node22.isLeaf());


        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        tree.add(node31, node32);


        assertFalse(node11.isLeaf());
        assertFalse(node21.isLeaf());
        assertFalse(node22.isLeaf());
        assertTrue(node31.isLeaf());
        assertTrue(node32.isLeaf());
    }

    @Test
    void getPathsSimple() {
        Tree<String> tree = new Tree<>();
        Node<String> node11 = new Node<>("11");
        tree.add(node11);

        Node<String> node21 = new Node<>("21");
        tree.add(node21);

        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        tree.add(node31, node32);

        ArrayList<Stack<Node<String>>> paths = tree.getPaths();

        assertEquals(2, paths.size());

        Stack<Node<String>> path1 = paths.get(0);
        assertEquals(3, path1.size());

        assertEquals(path1.pop(), node11);
        assertEquals(path1.pop(), node21);
        assertEquals(path1.pop(), node31);

        Stack<Node<String>> path2 = paths.get(1);
        assertEquals(3, path2.size());

        assertEquals(path2.pop(), node11);
        assertEquals(path2.pop(), node21);
        assertEquals(path2.pop(), node32);
    }

    @Test
    void getPathsMoreNodes() {
        Tree<String> tree = new Tree<>();
        Node<String> node11 = new Node<>("11");
        tree.add(node11);

        Node<String> node21 = new Node<>("21");
        tree.add(node21);

        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        tree.add(node31, node32);


        Node<String> node41 = new Node<>("41");
        Node<String> node42 = new Node<>("42");
        Node<String> node43 = new Node<>("43");
        tree.add(node41, node42, node43);

        ArrayList<Stack<Node<String>>> paths = tree.getPaths();

        assertEquals(6, paths.size());

        Stack<Node<String>> path1 = paths.get(0);
        assertEquals(4, path1.size());

        assertEquals(path1.pop(), node11);
        assertEquals(path1.pop(), node21);
        assertEquals(path1.pop(), node31);
        assertEquals(path1.pop(), node41);

        Stack<Node<String>> path2 = paths.get(1);
        assertEquals(4, path2.size());

        assertEquals(path2.pop(), node11);
        assertEquals(path2.pop(), node21);
        assertEquals(path2.pop(), node31);
        assertEquals(path2.pop(), node42);

        Stack<Node<String>> path3 = paths.get(2);
        assertEquals(4, path3.size());

        assertEquals(path3.pop(), node11);
        assertEquals(path3.pop(), node21);
        assertEquals(path3.pop(), node31);
        assertEquals(path3.pop(), node43);

        Stack<Node<String>> path4 = paths.get(3);
        assertEquals(4, path4.size());

        assertEquals(path4.pop(), node11);
        assertEquals(path4.pop(), node21);
        assertEquals(path4.pop(), node32);
        assertEquals(path4.pop(), node41);

        Stack<Node<String>> path5 = paths.get(4);
        assertEquals(4, path5.size());

        assertEquals(path5.pop(), node11);
        assertEquals(path5.pop(), node21);
        assertEquals(path5.pop(), node32);
        assertEquals(path5.pop(), node42);

        Stack<Node<String>> path6 = paths.get(5);
        assertEquals(4, path6.size());

        assertEquals(path6.pop(), node11);
        assertEquals(path6.pop(), node21);
        assertEquals(path6.pop(), node32);
        assertEquals(path6.pop(), node43);
    }

    @Test
    void getPathsManyNodes() {
        Tree<String> tree = new Tree<>();
        Node<String> node11 = new Node<>("11");
        tree.add(node11);

        Node<String> node21 = new Node<>("21");
        tree.add(node21);

        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        tree.add(node31, node32);

        Node<String> node41 = new Node<>("41");
        tree.add(node41);

        Node<String> node51 = new Node<>("51");
        Node<String> node52 = new Node<>("52");
        Node<String> node53 = new Node<>("53");
        tree.add(node51, node52, node53);

        ArrayList<Stack<Node<String>>> paths = tree.getPaths();

        assertEquals(6, paths.size());

        Stack<Node<String>> path1 = paths.get(0);
        assertEquals(5, path1.size());

        assertEquals(path1.pop(), node11);
        assertEquals(path1.pop(), node21);
        assertEquals(path1.pop(), node31);
        assertEquals(path1.pop(), node41);
        assertEquals(path1.pop(), node51);

        Stack<Node<String>> path2 = paths.get(1);
        assertEquals(5, path2.size());

        assertEquals(path2.pop(), node11);
        assertEquals(path2.pop(), node21);
        assertEquals(path2.pop(), node31);
        assertEquals(path2.pop(), node41);
        assertEquals(path2.pop(), node52);

        Stack<Node<String>> path3 = paths.get(2);
        assertEquals(5, path3.size());

        assertEquals(path3.pop(), node11);
        assertEquals(path3.pop(), node21);
        assertEquals(path3.pop(), node31);
        assertEquals(path3.pop(), node41);
        assertEquals(path3.pop(), node53);

        Stack<Node<String>> path4 = paths.get(3);
        assertEquals(5, path4.size());

        assertEquals(path4.pop(), node11);
        assertEquals(path4.pop(), node21);
        assertEquals(path4.pop(), node32);
        assertEquals(path4.pop(), node41);
        assertEquals(path4.pop(), node51);

        Stack<Node<String>> path5 = paths.get(4);
        assertEquals(5, path5.size());

        assertEquals(path5.pop(), node11);
        assertEquals(path5.pop(), node21);
        assertEquals(path5.pop(), node32);
        assertEquals(path5.pop(), node41);
        assertEquals(path5.pop(), node52);

        Stack<Node<String>> path6 = paths.get(5);
        assertEquals(5, path6.size());

        assertEquals(path6.pop(), node11);
        assertEquals(path6.pop(), node21);
        assertEquals(path6.pop(), node32);
        assertEquals(path6.pop(), node41);
        assertEquals(path6.pop(), node53);
    }
}