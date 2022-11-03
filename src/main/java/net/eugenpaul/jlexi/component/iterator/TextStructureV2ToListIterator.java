package net.eugenpaul.jlexi.component.iterator;

import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;

public class TextStructureV2ToListIterator<T extends TextStructureV2> implements ListIterator<TextStructureV2> {

    private ListIterator<T> iterator;

    public TextStructureV2ToListIterator(List<T> list) {
        this.iterator = list.listIterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public TextStructureV2 next() {
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
    public TextStructureV2 previous() {
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
    public void set(TextStructureV2 e) {
        iterator.set((T) e);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(TextStructureV2 e) {
        iterator.add((T) e);
    }

}
