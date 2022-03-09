package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TextStructureToIterator<T extends TextStructure> implements Iterator<TextStructure> {

    private Iterator<T> iterator;

    public TextStructureToIterator(List<T> list) {
        this.iterator = list.iterator();
    }

    public TextStructureToIterator(Collection<T> list) {
        this.iterator = list.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public TextStructure next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
