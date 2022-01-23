package net.eugenpaul.jlexi.component.iterator;

import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;

public class PreOrderLeefIterator implements GlyphIterator {

    private Glyph root;
    private LinkedList<GlyphIterator> iterators;

    public PreOrderLeefIterator(Glyph root) {
        this.root = root;
        iterators = new LinkedList<>();
    }

    @Override
    public void first() {
        iterators.clear();

        GlyphIterator childIterator = root.createIterator();
        childIterator.first();
        iterators.addLast(childIterator);
    }

    @Override
    public Glyph next() {
        GlyphIterator lastIterator = iterators.peekLast();
        Glyph lastGlyph = null;

        while (!iterators.isEmpty() && lastIterator.hasNext()) {
            lastGlyph = lastIterator.next();
            lastIterator = lastGlyph.createIterator();
            lastIterator.first();
            iterators.addLast(lastIterator);
        }

        while (!iterators.isEmpty() && !lastIterator.hasNext()) {
            iterators.pollLast();
            lastIterator = iterators.peekLast();
        }

        return lastGlyph;
    }

    @Override
    public boolean hasNext() {
        GlyphIterator lastIterator = iterators.peekLast();
        while (!iterators.isEmpty() && !lastIterator.hasNext()) {
            lastIterator = iterators.pollLast();
        }

        return !iterators.isEmpty();
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
