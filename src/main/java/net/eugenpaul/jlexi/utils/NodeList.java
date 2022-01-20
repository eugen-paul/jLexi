package net.eugenpaul.jlexi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class NodeList<E> {

    @AllArgsConstructor
    public static class NodeListElement<E> {
        @Getter
        private E data;

        @Getter(lombok.AccessLevel.PRIVATE)
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> next;

        @Getter(lombok.AccessLevel.PRIVATE)
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> prev;
        private NodeList<E> list;

        public void insertAfter(E data) {
            NodeListElement<E> node = new NodeListElement<>(data, this.getNext(), this, list);
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
}
