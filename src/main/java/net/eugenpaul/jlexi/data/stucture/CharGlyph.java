package net.eugenpaul.jlexi.data.stucture;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NullIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.data.window.Window;

public class CharGlyph implements Glyph {

    @Getter
    @Setter
    private char c;

    public CharGlyph(char c) {
        this.c = c;
    }

    @Override
    public Drawable draw(Window window) {
        return null;
    }

    @Override
    public Bounds getSize() {
        return null;
    }

    @Override
    public boolean isIntersects(Point point) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        // TODO Auto-generated method stub

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
    public GlyphIterator createIterator() {
        return NullIterator.getNullIterator();
    }

    @Override
    public Glyph parent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

}
