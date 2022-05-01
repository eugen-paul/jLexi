package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;

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
    public Drawable getDrawable() {
        if (component == null) {
            return DrawablePixelsImpl.EMPTY;
        }
        return component.getDrawable();
    }

    @Override
    public void visit(Visitor checker) {
        if (component == null) {
            return;
        }
        component.visit(checker);
    }

    @Override
    public Iterator<Glyph> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }
        return component.iterator();
    }
}
