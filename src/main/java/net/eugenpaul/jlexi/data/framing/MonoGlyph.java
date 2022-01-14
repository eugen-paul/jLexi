package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.data.window.Window;

/**
 * Abstract Class for Glyph that cann have just one child
 */
public abstract class MonoGlyph implements Glyph {

    private Glyph parent;
    private Glyph component;

    protected MonoGlyph(Glyph component) {
        this.component = component;
    }

    @Override
    public Drawable draw(Window window) {
        return component.draw(window);
    }

    @Override
    public Bounds getSize() {
        return component.getSize();
    }

    @Override
    public boolean isIntersects(Point point) {
        return component.isIntersects(point);
    }

    @Override
    public void insert(Glyph glyph, int position) {
        component.insert(glyph, position);
    }

    @Override
    public void remove(Glyph glyph) {
        component.remove(glyph);
    }

    @Override
    public Glyph child(int position) {
        return component.child(position);
    }

    @Override
    public Glyph parent() {
        return parent;
    }

    @Override
    public void visit(Visitor checker) {
        component.visit(checker);
    }
}
