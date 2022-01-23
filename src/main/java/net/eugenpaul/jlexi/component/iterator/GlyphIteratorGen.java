package net.eugenpaul.jlexi.component.iterator;

public interface GlyphIteratorGen<T> {
    public void first();

    public T next();

    public boolean hasNext();

    public void insertAfter();

    public void insertBefor();
}
