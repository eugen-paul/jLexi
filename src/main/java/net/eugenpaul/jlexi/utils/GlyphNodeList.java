package net.eugenpaul.jlexi.utils;

import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.TextPaneElementIterator;
import net.eugenpaul.jlexi.data.stucture.TextPaneElement;

public class GlyphNodeList extends NodeList<TextPaneElement> {

    public GlyphIterator iterator() {
        // Just testing. Do it better.
        return new TextPaneElementIterator(this);
    }
}
