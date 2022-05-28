package net.eugenpaul.jlexi.component.button;

import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.SingleGlyphCompositor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Button extends GuiGlyph {

    @Setter(value = AccessLevel.PROTECTED)
    @Getter(value = AccessLevel.PROTECTED)
    private SingleGlyphCompositor<Glyph> compositor;

    @Setter(value = AccessLevel.PROTECTED)
    private Glyph element;

    @Getter
    @Setter
    protected ButtonState state;

    protected Button(Glyph parent, Glyph element, SingleGlyphCompositor<Glyph> compositor) {
        super(parent);
        this.state = ButtonState.NORMAL;
        this.element = element;
        this.compositor = compositor;
    }

    protected Button(Glyph parent) {
        this(parent, null, null);
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size size) {
        // TODO Auto-generated method stub

    }

    @Override
    public Drawable getDrawable() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        if (compositor == null) {
            return DrawablePixelsImpl.EMPTY;
        }

        Glyph glyph = compositor.compose(element, getSize());

        cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        cachedDrawable.addDrawable(glyph.getDrawable(), 0, 0);

        return cachedDrawable.draw();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return List.of(element).iterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }
}
