package net.eugenpaul.jlexi.data.iterator;

import net.eugenpaul.jlexi.data.Glyph;

public interface GlyphIterator {
    public void first();

    public Glyph next();

    public boolean hasNext();

    public void insertAfter();

    public void insertBefor();
}
