package net.eugenpaul.jlexi.component.iterator;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Iterator for all Leaf-Classes (without child elements)
 * 
 * @param <T>
 */
public class NullIterator<T> implements ListIterator<T> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public T previous() {
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return 0;
    }

    @Override
    public void remove() {
        // Nothing to do
    }

    @Override
    public void set(T e) {
        // Nothing to do
    }

    @Override
    public void add(T e) {
        // Nothing to do
    }

}
