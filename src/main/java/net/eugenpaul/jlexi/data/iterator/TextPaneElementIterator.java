package net.eugenpaul.jlexi.data.iterator;

import net.eugenpaul.jlexi.data.stucture.TextPaneElement;
import net.eugenpaul.jlexi.utils.NodeList;
import net.eugenpaul.jlexi.utils.NodeList.NodeListElement;

public class TextPaneElementIterator implements GlyphIteratorGen<TextPaneElement> {

    NodeList<TextPaneElement> list;
    NodeListElement<TextPaneElement> elem;

    public TextPaneElementIterator(NodeList<TextPaneElement> list) {
        this.list = list;
        if (!list.isEmpty()) {
            elem = new NodeListElement<>(null, null, list.getNode(0), list);
        } else {
            elem = new NodeListElement<>(null, null, null, list);
        }
    }

    @Override
    public void first() {
        // nothing to do
    }

    @Override
    public TextPaneElement next() {
        elem = elem.getNext();
        return elem.getData();
    }

    @Override
    public boolean hasNext() {
        return elem.getNext() != null;
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
