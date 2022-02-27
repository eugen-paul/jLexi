package net.eugenpaul.jlexi.component.text.format;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;

public interface GlyphIterable<T extends Glyph> {
    public Iterator<T> printableIterator();
}
