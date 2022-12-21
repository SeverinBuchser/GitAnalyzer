package ch.unibe.inf.seg.gitanalyzer.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkablePointerClockTest {
    @Test
    void testSameBehaviourAsClockSimpleClock() {
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

        ConnectableIntersection<String> clock = new ConnectableIntersection<>(path, list);

        Iterator<String> pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item4, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item5, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item6, pathIterator.next());

        assertEquals(IntersectionChange.RESET, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());
        assertEquals(item2, pathIterator.next());
        assertEquals(item3, pathIterator.next());
        assertEquals(item4, pathIterator.next());
    }

    @Test
    void testSameBehaviourAsClockEmptyPath() {
        List<String> list = new ArrayList<>();

        String item1 = "1";
        String item2 = "2";
        String item3 = "3";

        list.add(item1);
        list.add(item2);
        list.add(item3);

        ConnectableIntersection<String> clock = new ConnectableIntersection<>(list);

        Iterator<String> pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item2, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item3, pathIterator.next());

        assertEquals(IntersectionChange.RESET, clock.changeDirection());
        pathIterator = clock.getPath().iterator();
        assertEquals(item1, pathIterator.next());
    }

    @Test
    void testTwoLinkedClocks() {
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
        clock1.connect(clock2);


        Iterator<String> pathIterator;
        pathIterator = clock1.getPath().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item12, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        assertEquals(IntersectionChange.CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item21, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item22, pathIterator.next());

        assertEquals(IntersectionChange.NO_CHANGE, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item13, pathIterator.next());
        assertEquals(item23, pathIterator.next());

        assertEquals(IntersectionChange.RESET, clock1.changeDirection());
        pathIterator = clock1.getPath().iterator();
        assertEquals(item11, pathIterator.next());
        assertEquals(item21, pathIterator.next());
    }
}