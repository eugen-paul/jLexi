package net.eugenpaul.jlexi.component.text;

import net.eugenpaul.jlexi.component.iterator.GlyphIteratorGen;
import net.eugenpaul.jlexi.utils.container.NodeList;

public class GlyphNodeList extends NodeList<TextPaneElement> {

    public GlyphIteratorGen<TextPaneElement> iterator() {
        return new TextPaneElementIterator(this);
    }
}
