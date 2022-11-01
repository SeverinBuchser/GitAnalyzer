package ch.unibe.inf.seg.mergeresolution.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PathBuilderTest {

    @Test
    void treeConstruction() {
        PathBuilder<String> builder = new PathBuilder<>();
        Node<String> node11 = new Node<>("11");
        builder.addLayer(node11);


        assertTrue(node11.isLeaf());


        Node<String> node21 = new Node<>("21");
        Node<String> node22 = new Node<>("22");
        builder.addLayer(node21, node22);


        assertFalse(node11.isLeaf());
        assertTrue(node21.isLeaf());
        assertTrue(node22.isLeaf());


        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        builder.addLayer(node31, node32);


        assertFalse(node11.isLeaf());
        assertFalse(node21.isLeaf());
        assertFalse(node22.isLeaf());
        assertTrue(node31.isLeaf());
        assertTrue(node32.isLeaf());
    }

    @Test
    void getPathsSimple() {
        PathBuilder<String> builder = new PathBuilder<>();
        Node<String> node11 = new Node<>("11");

        Node<String> node21 = new Node<>("21");

        Node<String> node31 = new Node<>("31");
        Node<String> node32 = new Node<>("32");
        builder.addLayer(node11);

        builder.addLayer(node21);
        builder.addLayer(node31, node32);

        assertEquals(2, builder.getPathCount());

        ArrayList<String> path;

        path = new Path<>(0, builder).build();
        assertEquals(3, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));

        path = new Path<>(1, builder).build();
        assertEquals(3, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
    }

    @Test
    void getPathsMoreNodes() {
        PathBuilder<String> builder = new PathBuilder<>();

        builder.addItemLayer("11");
        builder.addItemLayer("21");
        builder.addItemLayer("31", "32");
        builder.addItemLayer("41", "42", "43");

        assertEquals(6, builder.getPathCount());

        ArrayList<String> path;

        path = new Path<>(0, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("41", path.get(3));

        path = new Path<>(1, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("42", path.get(3));

        path = new Path<>(2, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("43", path.get(3));

        path = new Path<>(3, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("41", path.get(3));

        path = new Path<>(4, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("42", path.get(3));

        path = new Path<>(5, builder).build();
        assertEquals(4, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("43", path.get(3));
    }

    @Test
    void getPathsManyNodes() {
        PathBuilder<String> builder = new PathBuilder<>();

        builder.addItemLayer("11");
        builder.addItemLayer("21");
        builder.addItemLayer("31", "32");
        builder.addItemLayer("41");
        builder.addItemLayer("51", "52", "53");

        assertEquals(6, builder.getPathCount());

        ArrayList<String> path;

        path = new Path<>(0, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("51", path.get(4));

        path = new Path<>(1, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("52", path.get(4));

        path = new Path<>(2, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("31", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("53", path.get(4));

        path = new Path<>(3, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("51", path.get(4));

        path = new Path<>(4, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("52", path.get(4));

        path = new Path<>(5, builder).build();
        assertEquals(5, path.size());

        assertEquals("11", path.get(0));
        assertEquals("21", path.get(1));
        assertEquals("32", path.get(2));
        assertEquals("41", path.get(3));
        assertEquals("53", path.get(4));
    }

    @Test
    void getPathCount() {
        PathBuilder<String> builder = new PathBuilder<>();
        builder.addItemLayer("11");
        builder.addItemLayer("21");
        builder.addItemLayer("31", "32");
        builder.addItemLayer("41");
        builder.addItemLayer("51", "52", "53");

        assertEquals(6, builder.getPathCount());
    }
}