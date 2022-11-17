package net.eugenpaul.jlexi.component.iterator;

import java.util.Iterator;
import java.util.LinkedList;

public class PreOrderLeafIterator<T extends Iterable<T>> implements Iterator<T> {

    private LinkedList<Iterator<T>> iterators;

    public PreOrderLeafIterator(T root) {
        iterators = new LinkedList<>();

        Iterator<T> childIterator = root.iterator();
        iterators.addLast(childIterator);
    }

    @Override
    public T next() {
        Iterator<T> lastIterator = iterators.peekLast();
        T lastGlyph = null;

        while (!iterators.isEmpty() && lastIterator.hasNext()) {
            lastGlyph = lastIterator.next();
            lastIterator = lastGlyph.iterator();
            iterators.addLast(lastIterator);
        }

        while (!iterators.isEmpty() && !lastIterator.hasNext()) {
            iterators.pollLast();
            lastIterator = iterators.peekLast();
        }

        return lastGlyph;
    }

    @Override
    public boolean hasNext() {
        Iterator<T> lastIterator = iterators.peekLast();
        while (!iterators.isEmpty() && !lastIterator.hasNext()) {
            lastIterator = iterators.pollLast();
        }

        return !iterators.isEmpty();
    }
}
