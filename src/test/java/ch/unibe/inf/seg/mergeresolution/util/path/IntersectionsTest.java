package ch.unibe.inf.seg.mergeresolution.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionsTest {
    @Test
    void testTwoClocks() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String item11 = "11";
        String item12 = "12";
        String item13 = "13";
        String item21 = "21";
        String item22 = "22";
        String item23 = "23";

        list1.add(item11);
        list1.add(item12);
        list1.add(item13);

        list2.add(item21);
        list2.add(item22);
        list2.add(item23);

        ConnectableIntersection<String> clock1 = new ConnectableIntersection<>(list1);
        ConnectableIntersection<String> clock2 = new ConnectableIntersection<>(list2);
        Intersections<String> intersections = new Intersections<>();
        intersections.connect(clock1);
        intersections.connect(clock2);


        Iterator<Path<String>> iterator = intersections.iterator();
        Iterator<String> pathIterator;

        pathIterator = iterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = iterator.next().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        assertFalse(iterator.hasNext());

        assertEquals(9, intersections.size());
    }

    @Test
    void testClocksOfClocks() {
        // generate items
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();

        String item11 = "11";
        String item12 = "12";
        String item21 = "21";
        String item22 = "22";
        String item31 = "31";
        String item32 = "32";
        String item41 = "41";
        String item42 = "42";

        list1.add(item11);
        list1.add(item12);

        list2.add(item21);
        list2.add(item22);

        list3.add(item31);
        list3.add(item32);

        list4.add(item41);
        list4.add(item42);

        // generating first clocks
        ConnectableIntersection<String> intersection1 = new ConnectableIntersection<>(list1);
        ConnectableIntersection<String> intersection2 = new ConnectableIntersection<>(list2);

        Intersections<String> intersections1 = new Intersections<>();
        intersections1.connect(intersection1);
        intersections1.connect(intersection2);

        ConnectableIntersection<Path<String>> intersection5 = new ConnectableIntersection<>(intersections1);

        // generating second clocks
        ConnectableIntersection<String> intersection3 = new ConnectableIntersection<>(list3);
        ConnectableIntersection<String> intersection4 = new ConnectableIntersection<>(list4);

        Intersections<String> intersections2 = new Intersections<>();
        intersections2.connect(intersection3);
        intersections2.connect(intersection4);

        ConnectableIntersection<Path<String>> intersection6 = new ConnectableIntersection<>(intersections2);

        // combining clocks to form a master clocks
        Intersections<Path<String>> intersections = new Intersections<>();
        intersections.connect(intersection5);
        intersections.connect(intersection6);



        Iterator<Path<Path<String>>> iterator = intersections.iterator();
        Iterator<Path<String>> intersectionIterator;
        Iterator<String> pathIterator;


        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item31, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item41, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        intersectionIterator = iterator.next().iterator();


        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        pathIterator = intersectionIterator.next().iterator();
        assertEquals(item32, pathIterator.next());
        assertEquals(item42, pathIterator.next());


        assertFalse(intersectionIterator.hasNext());
        assertFalse(iterator.hasNext());

        assertEquals(16, intersections.size());

    }
}