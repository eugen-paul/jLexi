package net.eugenpaul.jlexi.component.iterator;

import net.eugenpaul.jlexi.component.Glyph;

public class NullIterator implements GlyphIterator {

    private static final NullIterator ITERATOR = new NullIterator();

    public static NullIterator getNullIterator() {
        return ITERATOR;
    }

    @Override
    public void first() {
        // nothing to do
    }

    @Override
    public Glyph next() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public void insertAfter() {
        // nothing to do
    }

    @Override
    public void insertBefor() {
        // nothing to do
    }

}
