package net.eugenpaul.jlexi.data.iterator;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.data.Glyph;

public class ListIterator implements GlyphIterator {

    private List<Glyph> children;
    private Iterator<Glyph> iterator;

    public ListIterator(List<Glyph> children) {
        this.children = children;
    }

    @Override
    public void first() {
        iterator = children.iterator();
    }

    @Override
    public Glyph next() {
        return iterator.next();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

}
