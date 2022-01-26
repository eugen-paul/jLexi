package net.eugenpaul.jlexi.utils.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

class NodeListTest {

    private NodeList<Integer> testlist;

    @BeforeEach
    void init() {
        testlist = new NodeList<>();
    }

    @Test
    void testAddFirst_one_object() {
        var data = testlist.addFirst(10);
        checkOneElementList(data);
    }

    private void checkOneElementList(NodeListElement<Integer> element) {
        assertNotNull(element);
        assertEquals(10, element.getData().intValue());
        assertNull(element.getPrev());
        assertNull(element.getNext());
        assertEquals(1, testlist.size());

        var first = testlist.getFirst();
        assertEquals(10, first.intValue());

        var last = testlist.getLast();
        assertEquals(10, last.intValue());
    }

    @Test
    void testAddFirst_three_objects() {
        var data3 = testlist.addFirst(30);
        var data2 = testlist.addFirst(20);
        var data1 = testlist.addFirst(10);
        checkThreeElementsList(data3, data2, data1);
    }

    private void checkThreeElementsList(NodeListElement<Integer> data3, NodeListElement<Integer> data2,
            NodeListElement<Integer> data1) {
        assertNotNull(data1);
        assertNotNull(data2);
        assertNotNull(data3);

        assertNull(data1.getPrev());
        assertNotNull(data1.getNext());
        assertEquals(20, data1.getNext().getData());

        assertNotNull(data2.getPrev());
        assertNotNull(data2.getNext());
        assertEquals(10, data2.getPrev().getData());
        assertEquals(30, data2.getNext().getData());

        assertNotNull(data3.getPrev());
        assertNull(data3.getNext());
        assertEquals(20, data3.getPrev().getData());

        assertEquals(3, testlist.size());

        assertEquals(10, testlist.getFirst().intValue());
        assertEquals(30, testlist.getLast().intValue());
    }

    @Test
    void testAddLast_one_object() {
        var data = testlist.addLast(10);
        checkOneElementList(data);
    }

    @Test
    void testAddLast_three_objects() {
        var data1 = testlist.addLast(10);
        var data2 = testlist.addLast(20);
        var data3 = testlist.addLast(30);

        checkThreeElementsList(data3, data2, data1);
    }

    @Test
    void testClear() {
        var data1 = testlist.addLast(10);
        var data2 = testlist.addLast(20);
        var data3 = testlist.addLast(30);

        assertEquals(3, testlist.size());
        assertFalse(data1.isRemoved());
        assertFalse(data2.isRemoved());
        assertFalse(data3.isRemoved());

        testlist.clear();

        assertEquals(0, testlist.size());
        assertTrue(data1.isRemoved());
        assertTrue(data2.isRemoved());
        assertTrue(data3.isRemoved());
    }

    @Test
    void testGet() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertEquals(3, testlist.size());

        assertEquals(10, testlist.get(0));
        assertEquals(20, testlist.get(1));
        assertEquals(30, testlist.get(2));
    }

    @Test
    void testGet_Exception() {
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.get(10));

        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertEquals(3, testlist.size());

        assertThrows(IndexOutOfBoundsException.class, () -> testlist.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.get(10));
    }

    @Test
    void testGetFirst() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertNotNull(testlist.getFirst());
        assertEquals(10, testlist.getFirst());
    }

    @Test
    void testGetFirstNode() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertNotNull(testlist.getFirstNode());
        assertEquals(10, testlist.getFirstNode().getData());
    }

    @Test
    void testGetLast() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertNotNull(testlist.getLast());
        assertEquals(30, testlist.getLast());
    }

    @Test
    void testGetLastNode() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertNotNull(testlist.getLastNode());
        assertEquals(30, testlist.getLastNode().getData());
    }

    @Test
    void testGetNode() {
        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertEquals(3, testlist.size());

        assertEquals(10, testlist.getNode(0).getData());
        assertEquals(20, testlist.getNode(1).getData());
        assertEquals(30, testlist.getNode(2).getData());
    }

    @Test
    void testGetNode_Exception() {
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.getNode(0));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.getNode(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.getNode(10));

        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertEquals(3, testlist.size());

        assertThrows(IndexOutOfBoundsException.class, () -> testlist.getNode(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> testlist.getNode(10));
    }

    @Test
    void testIsEmpty() {
        assertTrue(testlist.isEmpty());

        testlist.addLast(10);
        testlist.addLast(20);
        testlist.addLast(30);

        assertFalse(testlist.isEmpty());

        testlist.clear();

        assertTrue(testlist.isEmpty());
    }

}
