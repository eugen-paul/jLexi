package net.eugenpaul.jlexi.data.iterator;

import net.eugenpaul.jlexi.data.Glyph;

public class NullIterator implements GlyphIterator {

    private static final NullIterator ITERATOR = new NullIterator();

    public static NullIterator getNullIterator(){
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

}
