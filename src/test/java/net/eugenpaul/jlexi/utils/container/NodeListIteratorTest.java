package net.eugenpaul.jlexi.utils.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodeListIteratorTest {
    private NodeList<Integer> testlist;
    private ListIterator<Integer> testIterator;

    @BeforeEach
    void init() {
        testlist = new NodeList<>();
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        testIterator = testlist.iterator();
    }

    @Test
    void testAdd() {
        testIterator.add(1);

        assertEquals(4, testlist.size());
        assertEquals(1, testlist.get(0));
        assertEquals(10, testlist.get(1));
        assertEquals(20, testlist.get(2));
        assertEquals(30, testlist.get(3));
    }

    @Test
    void testHasNext() {
        assertTrue(testIterator.hasNext());
        assertNotNull(testIterator.next());

        assertTrue(testIterator.hasNext());
        assertNotNull(testIterator.next());

        assertTrue(testIterator.hasNext());
        assertNotNull(testIterator.next());

        assertFalse(testIterator.hasNext());
    }

    @Test
    void testHasPrevious() {
        assertFalse(testIterator.hasPrevious());
        assertNotNull(testIterator.next());

        assertTrue(testIterator.hasPrevious());
        assertNotNull(testIterator.next());

        assertTrue(testIterator.hasPrevious());
        assertNotNull(testIterator.next());

        assertTrue(testIterator.hasPrevious());
    }

    @Test
    void testNext() {
        assertTrue(testIterator.hasNext());
        assertEquals(10, testIterator.next());

        assertTrue(testIterator.hasNext());
        assertEquals(20, testIterator.next());

        assertTrue(testIterator.hasNext());
        assertEquals(30, testIterator.next());
    }

    @Test
    void testNext_exception() {
        assertEquals(10, testIterator.next());
        assertEquals(20, testIterator.next());
        assertEquals(30, testIterator.next());

        assertThrows(NoSuchElementException.class, testIterator::next);
    }

    @Test
    void testNextIndex() {
        assertEquals(0, testIterator.nextIndex());
        assertEquals(10, testIterator.next());

        assertEquals(1, testIterator.nextIndex());
        assertEquals(20, testIterator.next());

        assertEquals(2, testIterator.nextIndex());
        assertEquals(30, testIterator.next());

        assertEquals(3, testIterator.nextIndex());

        assertEquals(30, testIterator.previous());
        assertEquals(2, testIterator.nextIndex());

        assertEquals(20, testIterator.previous());
        assertEquals(1, testIterator.nextIndex());

        assertEquals(10, testIterator.previous());
        assertEquals(0, testIterator.nextIndex());
    }

    @Test
    void testPrevious() {
        assertEquals(10, testIterator.next());
        assertEquals(20, testIterator.next());
        assertEquals(30, testIterator.next());

        assertEquals(30, testIterator.previous());
        assertEquals(20, testIterator.previous());
        assertEquals(10, testIterator.previous());
    }

    @Test
    void testPrevious_exception() {
        assertThrows(NoSuchElementException.class, testIterator::previous);

        assertEquals(10, testIterator.next());
        assertEquals(20, testIterator.next());
        assertEquals(30, testIterator.next());

        assertEquals(30, testIterator.previous());
        assertEquals(20, testIterator.previous());
        assertEquals(10, testIterator.previous());

        assertThrows(NoSuchElementException.class, testIterator::previous);
    }

    @Test
    void testPreviousIndex() {
        assertEquals(-1, testIterator.previousIndex());
        assertEquals(10, testIterator.next());

        assertEquals(0, testIterator.previousIndex());
        assertEquals(20, testIterator.next());

        assertEquals(1, testIterator.previousIndex());
        assertEquals(30, testIterator.next());

        assertEquals(2, testIterator.previousIndex());

        assertEquals(30, testIterator.previous());
        assertEquals(1, testIterator.previousIndex());

        assertEquals(20, testIterator.previous());
        assertEquals(0, testIterator.previousIndex());

        assertEquals(10, testIterator.previous());
        assertEquals(-1, testIterator.previousIndex());
    }

    @Test
    void testRemove() {
        assertEquals(3, testlist.size());
        assertThrows(IllegalStateException.class, testIterator::remove);
        assertEquals(3, testlist.size());

        assertEquals(10, testIterator.next());
        assertEquals(20, testIterator.next());

        testIterator.remove();

        assertEquals(2, testlist.size());
        assertThrows(IllegalStateException.class, testIterator::remove);
        assertEquals(2, testlist.size());

        assertEquals(10, testlist.get(0));
        assertEquals(30, testlist.get(1));

        assertEquals(30, testIterator.next());
        assertEquals(30, testIterator.previous());
        assertEquals(10, testIterator.previous());

        assertThrows(NoSuchElementException.class, testIterator::previous);
    }

    @Test
    void testRemove_First() {
        assertEquals(3, testlist.size());
        assertThrows(IllegalStateException.class, testIterator::remove);
        assertEquals(3, testlist.size());

        assertEquals(10, testIterator.next());

        testIterator.remove();

        assertEquals(2, testlist.size());
        assertThrows(IllegalStateException.class, testIterator::remove);
        assertEquals(2, testlist.size());

        assertEquals(20, testlist.get(0));
        assertEquals(30, testlist.get(1));

        assertEquals(20, testlist.getFirst());
        assertEquals(30, testlist.getLast());

        assertEquals(20, testIterator.next());
        assertEquals(30, testIterator.next());
        assertEquals(30, testIterator.previous());
        assertEquals(20, testIterator.previous());

        assertThrows(NoSuchElementException.class, testIterator::previous);
    }

    @Test
    void testSet() {
        assertThrows(IllegalStateException.class, () -> testIterator.set(5));

        assertEquals(3, testlist.size());

        assertEquals(10, testIterator.next());
        testIterator.set(5);
        assertEquals(3, testlist.size());
        
        assertEquals(5, testlist.getFirst());
        
        assertEquals(20, testIterator.next());
        assertEquals(20, testIterator.previous());
        assertEquals(5, testIterator.previous());
        assertEquals(5, testIterator.next());
        assertEquals(20, testIterator.next());
        assertEquals(20, testIterator.previous());
        
        testIterator.set(15);
        assertEquals(30, testIterator.next());

        testIterator.set(25);

        assertEquals(25, testlist.getLast());
        assertEquals(15, testlist.getLastNode().getPrev().getData());
    }
}
