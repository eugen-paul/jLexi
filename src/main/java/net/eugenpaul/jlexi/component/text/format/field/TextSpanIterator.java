package net.eugenpaul.jlexi.component.text.format.field;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextSpanIterator implements Iterator<TextElement> {

    private Iterator<TextField> rootIterator;
    private Iterator<TextElement> currentChildIterator;

    public TextSpanIterator(List<TextField> fields) {
        rootIterator = fields.iterator();
        if (rootIterator.hasNext()) {
            currentChildIterator = rootIterator.next().printableIterator();
        } else {
            currentChildIterator = Collections.emptyIterator();
        }
    }

    @Override
    public TextElement next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (currentChildIterator.hasNext()) {
            return currentChildIterator.next();
        }

        if (rootIterator.hasNext()) {
            currentChildIterator = rootIterator.next().printableIterator();
            if (currentChildIterator.hasNext()) {
                return currentChildIterator.next();
            }
        }

        throw new NoSuchElementException();
    }

    @Override
    public boolean hasNext() {
        return currentChildIterator.hasNext() || rootIterator.hasNext();
    }
}
