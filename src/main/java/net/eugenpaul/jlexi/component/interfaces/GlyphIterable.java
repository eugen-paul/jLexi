package net.eugenpaul.jlexi.component.interfaces;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;

public interface GlyphIterable<T extends Glyph> {
    public Iterator<T> drawableChildIterator();
}
