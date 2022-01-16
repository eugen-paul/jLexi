package net.eugenpaul.jlexi.data.framing;

import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

public class TextPane extends Composition<Glyph> implements Resizeable {

    private static final int BACKGROUND_COLOR = 0xFF000000;
    private List<Glyph> children;

    private Size size;

    protected TextPane() {
        super(new RowCompositor());
        size = Size.ZERO_SIZE;
        children = new ArrayList<>();
        compositor.setComposition(this);
    }

    @Override
    public Drawable getPixels() {
        Glyph textField = compositor.compose(children);
        return textField.getPixels();
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public boolean isIntersects(Point point) {
        return false;
    }

    @Override
    public void remove(Glyph glyph) {
        // Nothing to do
    }

    @Override
    public Glyph child(int position) {
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return null;
    }

    @Override
    public Glyph parent() {
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        children.add(position, glyph);
    }

}
