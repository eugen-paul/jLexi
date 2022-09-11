package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttrReadIterator<T extends Map.Entry<String, String>> implements Iterator<Map.Entry<String, String>> {

    private Iterator<T> iterator;

    public AttrReadIterator(List<T> list) {
        this.iterator = list.iterator();
    }

    public AttrReadIterator(Collection<T> list) {
        this.iterator = list.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Map.Entry<String, String> next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
