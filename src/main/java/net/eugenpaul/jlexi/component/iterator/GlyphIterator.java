package net.eugenpaul.jlexi.component.iterator;

import net.eugenpaul.jlexi.component.Glyph;

public interface GlyphIterator {
    public void first();

    public Glyph next();

    public boolean hasNext();

    public void insertAfter();

    public void insertBefor();
}
