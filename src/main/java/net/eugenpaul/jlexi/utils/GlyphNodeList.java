package net.eugenpaul.jlexi.utils;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NodeListIterator;

public class GlyphNodeList extends NodeList<Glyph> {

    public GlyphIterator iterator() {
        // Just testing. Do it better.
        return new NodeListIterator(this);
    }
}
