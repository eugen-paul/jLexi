package net.eugenpaul.jlexi.component.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextElementToGlyphIterator<T extends TextElement> implements Iterator<Glyph> {

    private Iterator<T> iterator;

    public TextElementToGlyphIterator(List<T> list) {
        this.iterator = list.iterator();
    }

    public TextElementToGlyphIterator(Collection<T> list) {
        this.iterator = list.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Glyph next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
