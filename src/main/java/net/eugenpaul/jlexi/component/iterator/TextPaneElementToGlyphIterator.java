package net.eugenpaul.jlexi.component.iterator;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;

public class TextPaneElementToGlyphIterator<T extends TextPaneElement> implements Iterator<Glyph> {

    private Iterator<T> iterator;

    public TextPaneElementToGlyphIterator(List<T> list) {
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
