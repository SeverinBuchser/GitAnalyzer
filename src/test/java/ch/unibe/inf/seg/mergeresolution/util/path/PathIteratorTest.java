package ch.unibe.inf.seg.mergeresolution.util.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class PathIteratorTest {

    PathBuilder<Integer> tree = new PathBuilder<>();

    Node<Integer> n1 = new Node<>(1);
    Node<Integer> n2 = new Node<>(2);
    Node<Integer> n3 = new Node<>(3);
    Node<Integer> n4 = new Node<>(4);
    Node<Integer> n5 = new Node<>(5);
    Node<Integer> n6 = new Node<>(6);

    @BeforeEach
    void createTree() {
        tree = new PathBuilder<>();
        n1 = new Node<>(1);
        n2 = new Node<>(2);
        n3 = new Node<>(3);
        n4 = new Node<>(4);
        n5 = new Node<>(5);
        n6 = new Node<>(6);

        tree.addLayer(n1, n2);
        tree.addLayer(n3, n4);
        tree.addLayer(n5, n6);
    }

    @Test
    void basicTree() {
        Iterator<Path<Integer>> pathIterator = tree.iterator();

        ArrayList<Integer> path;

        path = pathIterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(3, path.get(1));
        assertEquals(5, path.get(2));

        path = pathIterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(3, path.get(1));
        assertEquals(6, path.get(2));

        path = pathIterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(4, path.get(1));
        assertEquals(5, path.get(2));

        path = pathIterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(4, path.get(1));
        assertEquals(6, path.get(2));

        path = pathIterator.next().build();
        assertEquals(2, path.get(0));
        assertEquals(3, path.get(1));
        assertEquals(5, path.get(2));

        path = pathIterator.next().build();
        assertEquals(2, path.get(0));
        assertEquals(3, path.get(1));
        assertEquals(6, path.get(2));

        path = pathIterator.next().build();
        assertEquals(2, path.get(0));
        assertEquals(4, path.get(1));
        assertEquals(5, path.get(2));

        path = pathIterator.next().build();
        assertEquals(2, path.get(0));
        assertEquals(4, path.get(1));
        assertEquals(6, path.get(2));
    }

    @Test
    void emptyTreeTest() {
        PathBuilder<Integer> tree = new PathBuilder<>();
        Iterator<Path<Integer>> iterator = tree.iterator();
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.next().build().size());
    }

    @Test
    void oneNodeTreeTest() {
        PathBuilder<Integer> tree = new PathBuilder<>();
        tree.addLayer(new Node<>(1));
        Iterator<Path<Integer>> iterator = tree.iterator();

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().build().get(0));

        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.next().build().size());
    }



    @Test
    void threeNodesTreeTest() {
        PathBuilder<Integer> tree = new PathBuilder<>();
        tree.addLayer(new Node<>(1));
        tree.addLayer(new Node<>(2), new Node<>(3));
        Iterator<Path<Integer>> iterator = tree.iterator();


        ArrayList<Integer> path;

        assertTrue(iterator.hasNext());

        path = iterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(2, path.get(1));

        assertTrue(iterator.hasNext());

        path = iterator.next().build();
        assertEquals(1, path.get(0));
        assertEquals(3, path.get(1));

        assertFalse(iterator.hasNext());
    }

}