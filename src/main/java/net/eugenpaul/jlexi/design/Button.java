package net.eugenpaul.jlexi.design;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Button extends Glyph implements MouseClickable {

    @Setter(value = AccessLevel.PROTECTED)
    private GlyphCompositor<Glyph> compositor;

    @Getter(value = AccessLevel.PROTECTED)
    private List<Glyph> elements;

    @Getter
    @Setter
    protected ButtonState state;

    @Setter
    protected MouseEventAdapter mouseEventAdapter;

    protected Button(Glyph parent, List<Glyph> elements, GlyphCompositor<Glyph> compositor) {
        super(parent);
        this.state = ButtonState.NORMAL;
        this.elements = elements;
        this.compositor = compositor;

        this.mouseEventAdapter = new MouseEventAdapter() {

        };
    }

    protected Button(Glyph parent) {
        this(parent, new LinkedList<>(), null);
    }

    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        mouseEventAdapter.mouseClicked(button);
    }

    @Override
    public Drawable getPixels() {
        if (cachedDrawable != null) {
            return cachedDrawable;
        }

        if (compositor == null) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        List<Glyph> glyph = compositor.compose(elements.iterator(), size);

        if (glyph.isEmpty()) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        cachedDrawable = glyph.get(0).getPixels();
        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return elements.iterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

    public void redraw() {
        cachedDrawable = null;
        if (parent != null) {
            parent.notifyRedraw(getPixels(), getRelativPosition(), size);
        }
    }
}
