package net.eugenpaul.jlexi.data.stucture;

import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.ListIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.data.window.Window;

public class Row implements Glyph {

    private List<Glyph> children;

    public Row() {
        children = new ArrayList<>();
    }

    @Override
    public Drawable draw(Window window) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bounds getSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isIntersects(Point point) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        children.add(position, glyph);
    }

    @Override
    public void remove(Glyph glyph) {
        // TODO Auto-generated method stub

    }

    @Override
    public Glyph child(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Glyph parent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return new ListIterator(children);
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

}
