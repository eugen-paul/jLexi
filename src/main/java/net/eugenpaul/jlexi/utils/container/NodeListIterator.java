package net.eugenpaul.jlexi.utils.container;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class NodeListIterator<E> implements ListIterator<E> {

    private NodeList<E> list;
    private NodeListElement<E> next;
    private NodeListElement<E> prev;
    private NodeListElement<E> currentElement;
    private int cursorIndex;

    public NodeListIterator(NodeList<E> list) {
        this.list = list;
        this.next = list.getFirstNode();
        this.currentElement = null;
        this.prev = null;

        this.cursorIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentElement = next;
        next = next.getNext();
        prev = currentElement;
        cursorIndex++;
        return currentElement.getData();
    }

    @Override
    public boolean hasPrevious() {
        return prev != null;
    }

    @Override
    public E previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        currentElement = prev;
        next = currentElement;
        prev = prev.getPrev();
        cursorIndex--;
        return currentElement.getData();
    }

    @Override
    public int nextIndex() {
        return cursorIndex;
    }

    @Override
    public int previousIndex() {
        return cursorIndex - 1;
    }

    @Override
    public void remove() {
        if (currentElement == null) {
            throw new IllegalStateException();
        }
        cursorIndex--;
        currentElement.remove();
        currentElement = null;
    }

    @Override
    public void set(E e) {
        if (currentElement == null) {
            throw new IllegalStateException();
        }
        next = currentElement.getNext();
        prev = currentElement.getPrev();

        currentElement.remove();
        if (prev == null) {
            currentElement = list.addFirst(e);
        } else {
            currentElement = prev.insertAfter(e);
        }
    }

    @Override
    public void add(E e) {
        if (currentElement == null) {
            list.addFirst(e);
        } else {
            currentElement.insertAfter(e);
        }
    }

}
