package net.eugenpaul.jlexi.component.button;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Button extends GuiGlyph {

    @Setter(value = AccessLevel.PROTECTED)
    @Getter(value = AccessLevel.PROTECTED)
    private GlyphCompositor<Glyph> compositor;

    @Getter(value = AccessLevel.PROTECTED)
    private List<Glyph> elements;

    @Getter
    @Setter
    protected ButtonState state;

    protected Button(Glyph parent, List<Glyph> elements, GlyphCompositor<Glyph> compositor) {
        super(parent);
        this.state = ButtonState.NORMAL;
        this.elements = elements;
        this.compositor = compositor;
    }

    protected Button(Glyph parent) {
        this(parent, new LinkedList<>(), null);
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
    public DrawableV2 getDrawable() {
        if (cachedDrawableV2 != null) {
            return cachedDrawableV2.draw();
        }

        if (compositor == null) {
            return DrawableV2PixelsImpl.EMPTY;
        }

        List<Glyph> glyph = compositor.compose(elements.iterator(), getSize());

        if (glyph.isEmpty()) {
            return DrawableV2PixelsImpl.EMPTY;
        }

        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE);
        var drawable = glyph.get(0).getDrawable();
        cachedDrawableV2.addDrawable(drawable, 0, 0);

        return cachedDrawableV2.draw();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return elements.iterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }
}
