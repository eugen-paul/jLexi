package net.eugenpaul.jlexi.utils.container;

import net.eugenpaul.jlexi.component.iterator.GlyphIteratorGen;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class NodeListIterator<E> implements GlyphIteratorGen<E> {

    NodeList<E> list;
    NodeListElement<E> elem;

    public NodeListIterator(NodeList<E> list) {
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
    public E next() {
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
