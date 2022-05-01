package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;

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
        if (component == null) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }
        return component.getPixels();
    }

    @Override
    public DrawableV2 getDrawable() {
        if (component == null) {
            return DrawableV2PixelsImpl.EMPTY;
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
