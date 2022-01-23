package net.eugenpaul.jlexi.component.iterator;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;

public class ListIteratorGen<T extends Glyph> implements GlyphIterator {

    private List<T> children;
    private Iterator<T> iterator;

    public ListIteratorGen(List<T> children) {
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

    @Override
    public void insertAfter() {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertBefor() {
        // TODO Auto-generated method stub

    }

}
