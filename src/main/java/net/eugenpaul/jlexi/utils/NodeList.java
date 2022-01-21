package net.eugenpaul.jlexi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class NodeList<E> {

    @AllArgsConstructor
    public static class NodeListElement<E> {
        @Getter
        private E data;

        @Getter
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> prev;

        @Getter
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> next;

        private NodeList<E> list;

        public void insertAfter(E data) {
            NodeListElement<E> node = new NodeListElement<>(data, this, this.getNext(), list);
            if (null != next) {
                next.setPrev(node);
            } else {
                list.last = node;
            }

            this.next = node;
            list.size++;
        }
    }

    private NodeListElement<E> first;
    private NodeListElement<E> last;
    private int size;

    public NodeList() {
        size = 0;
        first = null;
        last = null;
    }

    public E get(int position) {
        if (position > size) {
            throw new IndexOutOfBoundsException(
                    "Cann't get element on position " + position + ". List has only " + size + " elements.");
        }
        NodeListElement<E> current = first;
        int i = 0;
        while (i < position) {
            current = current.getNext();
            i++;
        }
        return current.data;
    }

    public NodeListElement<E> getNode(int position) {
        if (position > size) {
            throw new IndexOutOfBoundsException(
                    "Cann't get element on position " + position + ". List has only " + size + " elements.");
        }
        NodeListElement<E> current = first;
        int i = 0;
        while (i < position) {
            current = current.getNext();
            i++;
        }
        return current;
    }

    public E getFirst() {
        if (null != first) {
            return first.data;
        }
        return null;
    }

    public E getLast() {
        if (null != last) {
            return last.data;
        }
        return null;
    }

    public NodeListElement<E> addFirst(E data) {
        NodeListElement<E> node = new NodeListElement<>(data, null, first, this);
        if (size == 0) {
            last = node;
        }
        if (first != null) {
            first.setPrev(node);
        }
        first = node;
        size++;
        return node;
    }

    public NodeListElement<E> addLast(E data) {
        NodeListElement<E> node = new NodeListElement<>(data, last, null, this);
        if (size == 0) {
            first = node;
        }
        if (last != null) {
            last.setNext(node);
        }
        last = node;
        size++;
        return node;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
