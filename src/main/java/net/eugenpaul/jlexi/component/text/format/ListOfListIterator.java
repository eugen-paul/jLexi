package net.eugenpaul.jlexi.component.text.format;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.eugenpaul.jlexi.component.Glyph;

public class ListOfListIterator<E extends Glyph, R extends GlyphIterable<E>> implements Iterator<E> {

    private Iterator<R> rootIterator;
    private Iterator<E> currentChildIterator;

    public ListOfListIterator(List<R> fields) {
        rootIterator = fields.iterator();
        currentChildIterator = Collections.emptyIterator();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (currentChildIterator.hasNext()) {
            return currentChildIterator.next();
        }

        while (rootIterator.hasNext()) {
            currentChildIterator = rootIterator.next().printableIterator();
            if (currentChildIterator.hasNext()) {
                return currentChildIterator.next();
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        if (currentChildIterator.hasNext()) {
            return true;
        }

        while (rootIterator.hasNext()) {
            currentChildIterator = rootIterator.next().printableIterator();
            if (currentChildIterator.hasNext()) {
                return true;
            }
        }

        return false;
    }
}
