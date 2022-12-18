package ch.unibe.inf.seg.mergeresolution.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {
    @Test
    void testSimpleClock() {
        Path<String> path = new Path<>();

        String item1 = "1";
        String item2 = "2";
        String item3 = "3";

        path.add(item1);
        path.add(item2);
        path.add(item3);

        List<String> list = new ArrayList<>();

        String item4 = "4";
        String item5 = "5";
        String item6 = "6";

        list.add(item4);
        list.add(item5);
        list.add(item6);

        Intersection<String> intersection = new Intersection<>(path, list);

        Iterator<String> pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item4, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item5, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item6, pathIterator.next());

        assertEquals(IntersectionChange.RESET, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item4, pathIterator.next());

        assertEquals(3, intersection.size());
    }

    @Test
    void testEmptyPathClock() {
        List<String> list = new ArrayList<>();

        String item1 = "1";
        String item2 = "2";
        String item3 = "3";

        list.add(item1);
        list.add(item2);
        list.add(item3);

        Intersection<String> intersection = new Intersection<>(list);

        Iterator<String> pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item2, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item3, pathIterator.next());

        assertEquals(IntersectionChange.RESET, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item1, pathIterator.next());

        assertEquals(3, intersection.size());
    }

    @Test
    void testOneElementClock() {
        List<String> list = new ArrayList<>();
        String item = "item";
        list.add(item);

        Intersection<String> intersection = new Intersection<>(list);
        Iterator<String> pathIterator = intersection.getPath().iterator();
        assertEquals(item, pathIterator.next());
        assertFalse(pathIterator.hasNext());

        assertEquals(IntersectionChange.RESET, intersection.changeDirection());
        pathIterator = intersection.getPath().iterator();
        assertEquals(item, pathIterator.next());
        assertFalse(pathIterator.hasNext());

        assertEquals(1, intersection.size());
    }

    @Test
    void testEmptyClock() {
        Intersection<String> intersection = new Intersection<>(new Path<>(), Collections.emptyList());
        Iterator<String> pathIterator = intersection.getPath().iterator();
        assertFalse(pathIterator.hasNext());
    }
}