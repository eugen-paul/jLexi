package net.eugenpaul.jlexi.component.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import net.eugenpaul.jlexi.component.text.TextPaneElement;

public class TextPaneElemenIterator<T extends TextPaneElement> implements Iterator<TextPaneElement> {

    private Iterator<T> iterator;

    public TextPaneElemenIterator(List<T> list) {
        this.iterator = list.iterator();
    }

    public TextPaneElemenIterator(Collection<T> list) {
        this.iterator = list.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public TextPaneElement next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
