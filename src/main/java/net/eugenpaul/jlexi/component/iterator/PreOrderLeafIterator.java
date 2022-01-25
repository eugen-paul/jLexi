package net.eugenpaul.jlexi.component.iterator;

import java.util.Iterator;
import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;

public class PreOrderLeafIterator implements Iterator<Glyph> {

    private LinkedList<Iterator<Glyph>> iterators;

    public PreOrderLeafIterator(Glyph root) {
        iterators = new LinkedList<>();
        iterators.clear();

        Iterator<Glyph> childIterator = root.iterator();
        iterators.addLast(childIterator);
    }

    @Override
    public Glyph next() {
        Iterator<Glyph> lastIterator = iterators.peekLast();
        Glyph lastGlyph = null;

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
        Iterator<Glyph> lastIterator = iterators.peekLast();
        while (!iterators.isEmpty() && !lastIterator.hasNext()) {
            lastIterator = iterators.pollLast();
        }

        return !iterators.isEmpty();
    }
}
