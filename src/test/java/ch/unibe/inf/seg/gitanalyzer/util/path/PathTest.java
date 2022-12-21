package ch.unibe.inf.seg.gitanalyzer.util.path;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void testAppendList() {
        Path<String> path = new Path<>();

        String item11 = "11";
        String item12 = "12";
        String item13 = "13";

        path.add(item11);
        path.add(item12);
        path.add(item13);

        List<String> list = new ArrayList<>();

        String item21 = "21";
        String item22 = "22";
        String item23 = "23";

        list.add(item21);
        list.add(item22);
        list.add(item23);

        Path<String> newPath = path.append(list);

        Iterator<String> iterator = newPath.iterator();
        assertEquals(item11, iterator.next());
        assertEquals(item12, iterator.next());
        assertEquals(item13, iterator.next());
        assertEquals(item21, iterator.next());
        assertEquals(item22, iterator.next());
        assertEquals(item23, iterator.next());
        assertFalse(iterator.hasNext());
        assertNotEquals(path, newPath);
    }

    @Test
    void testAppendItems() {
        Path<String> path = new Path<>();

        String item11 = "11";
        String item12 = "12";
        String item13 = "13";

        String item21 = "21";
        String item22 = "22";

        path.add(item11);
        path.add(item12);
        path.add(item13);

        Path<String> newPath = path.append(item21).append(item22);

        Iterator<String> iterator = newPath.iterator();
        assertEquals(item11, iterator.next());
        assertEquals(item12, iterator.next());
        assertEquals(item13, iterator.next());
        assertEquals(item21, iterator.next());
        assertEquals(item22, iterator.next());
        assertFalse(iterator.hasNext());
        assertNotEquals(path, newPath);
    }
}