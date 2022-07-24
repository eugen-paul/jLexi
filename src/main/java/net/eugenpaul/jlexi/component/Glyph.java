package net.eugenpaul.jlexi.component;

import java.util.Iterator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Glyph {

    @Getter
    @Setter
    protected Glyph parent;

    @Getter
    @Setter
    protected Vector2d relativPosition;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    protected DrawableSketch cachedDrawable;

    @Getter
    @Setter(value = AccessLevel.PROTECTED)
    private Size size;

    /**
     * C'tor
     * 
     * @param parent
     */
    protected Glyph(Glyph parent) {
        this.parent = parent;
        this.relativPosition = new Vector2d(0, 0);
        this.size = Size.ZERO_SIZE;
        this.cachedDrawable = null;
    }

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public abstract Drawable getDrawable();

    /**
     * get a Iterator to iterate over children
     * 
     * @return Iterator
     */
    public abstract Iterator<Glyph> iterator();

    /**
     * Compute the relativposition of Element to given parentGlyph.
     * 
     * @param parentGlyph
     * @return Result or null if parentGlyph is not a parent.
     */
    public Vector2d getRelativPositionTo(Glyph parentGlyph) {
        Glyph pGlyph = parent;
        Vector2d responsePosition = new Vector2d(relativPosition);
        while (null != pGlyph) {
            if (pGlyph == parentGlyph) {
                break;
            }
            responsePosition.add(pGlyph.getRelativPosition());
            // responsePosition
            pGlyph = pGlyph.getParent();
        }

        if (null == pGlyph) {
            return null;
        }
        return responsePosition;
    }

    public Vector2d getRelativPositionToMainParent() {
        Glyph pGlyph = parent;
        Vector2d responsePosition = new Vector2d(relativPosition);
        while (null != pGlyph) {
            responsePosition.add(pGlyph.getRelativPosition());
            // responsePosition
            pGlyph = pGlyph.getParent();
        }

        return responsePosition;
    }

    /**
     * 
     * @param checker
     */
    public abstract void visit(Visitor checker);

    public void redraw() {
        this.cachedDrawable = null;

        if (parent == null) {
            return;
        }

        parent.redraw();
    }

    public void notifyChange() {
        this.cachedDrawable = null;

        if (parent == null) {
            return;
        }
        parent.notifyChange();
    }

    /**
     * The function returns the child node of the {@code parentGlyph} leading to the element.
     * 
     * @param parentGlyph
     * @return
     */
    public Glyph getChild(Glyph parentGlyph) {
        Glyph previousParent = null;
        while (parent != null && parent != parentGlyph) {
            previousParent = parent;
            parent = parent.getParent();
        }

        return previousParent;
    }

}
