package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

/**
 * Abstract Class for Glyph that cann have just one child
 */
public abstract class MonoGlyph extends Glyph {

    protected Glyph component;

    protected MonoGlyph(Glyph parent, Glyph component) {
        super(parent);
        this.component = component;
    }

    @Override
    public Drawable getPixels() {
        return component.getPixels();
    }

    @Override
    public void visit(Visitor checker) {
        component.visit(checker);
    }

    @Override
    public GlyphIterator createIterator() {
        return component.createIterator();
    }
}
