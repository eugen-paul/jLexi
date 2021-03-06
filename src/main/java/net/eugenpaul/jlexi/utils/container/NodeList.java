package net.eugenpaul.jlexi.utils.container;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import lombok.Getter;
import lombok.Setter;

public class NodeList<E> implements Iterable<E> {

    public static class NodeListElement<E> {
        @Getter
        private boolean removed;

        @Getter
        private E data;

        @Getter
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> prev;

        @Getter
        @Setter(lombok.AccessLevel.PRIVATE)
        private NodeListElement<E> next;

        private NodeList<E> list;

        public NodeListElement(E data, NodeListElement<E> prev, NodeListElement<E> next, NodeList<E> list) {
            this.data = data;
            this.prev = prev;
            this.next = next;
            this.list = list;

            removed = false;
        }

        public NodeListElement<E> insertAfter(E data) {
            if (removed) {
                throw new NoSuchElementException("\"insertAfter\" is not possible. Element is removed from list.");
            }

            NodeListElement<E> node = new NodeListElement<>(data, this, this.getNext(), list);
            if (null != next) {
                next.setPrev(node);
            } else {
                list.last = node;
            }

            this.next = node;
            list.size++;
            return node;
        }

        public NodeListElement<E> insertBefore(E data) {
            if (removed) {
                throw new NoSuchElementException("\"insertBefore\" is not possible. Element is removed from list.");
            }

            NodeListElement<E> node = new NodeListElement<>(data, this.getPrev(), this, list);
            if (null != prev) {
                prev.setNext(node);
            } else {
                list.first = node;
            }

            this.prev = node;
            list.size++;
            return node;
        }

        public void remove() {
            if (null != prev) {
                prev.setNext(next);
            } else {
                list.first = next;
            }

            if (null != next) {
                next.setPrev(prev);
            } else {
                list.last = prev;
            }

            prev = null;
            next = null;
            removed = true;
            list.size--;
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
        if (position >= size || position < 0) {
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
        if (position >= size || position < 0) {
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

    public NodeListElement<E> getFirstNode() {
        if (null != first) {
            return first;
        }
        return null;
    }

    public E getLast() {
        if (null != last) {
            return last.data;
        }
        return null;
    }

    public NodeListElement<E> getLastNode() {
        if (null != last) {
            return last;
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

    public void clear() {
        while (size > 0) {
            first.remove();
        }

        first = null;
        last = null;
    }

    @Override
    public ListIterator<E> iterator() {
        return new NodeListIterator<>(this);
    }

    public int size() {
        return size;
    }

}
