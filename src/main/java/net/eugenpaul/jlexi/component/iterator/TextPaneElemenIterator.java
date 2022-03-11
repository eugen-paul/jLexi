package net.eugenpaul.jlexi.component.iterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TextPaneElemenIterator<T> implements Iterator<T> {

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
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

}
