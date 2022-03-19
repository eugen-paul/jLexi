package net.eugenpaul.jlexi.component.iterator;

import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextStructureToListIterator<T extends TextStructure> implements ListIterator<TextStructure> {

    private ListIterator<T> iterator;

    public TextStructureToListIterator(List<T> list) {
        this.iterator = list.listIterator();
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

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public TextStructure previous() {
        return iterator.previous();
    }

    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(TextStructure e) {
        iterator.set((T) e);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(TextStructure e) {
        iterator.add((T) e);
    }

}
