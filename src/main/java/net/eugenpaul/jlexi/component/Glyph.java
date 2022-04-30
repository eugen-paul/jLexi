package net.eugenpaul.jlexi.component;

import java.util.Iterator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2Sketch;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public abstract class Glyph {

    @Getter
    @Setter
    protected Glyph parent;

    @Getter
    @Setter
    protected Vector2d relativPosition;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    protected Drawable cachedDrawable;

    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    protected DrawableV2Sketch cachedDrawableV2;

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
        this.cachedDrawableV2 = null;
    }

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public abstract Drawable getPixels();

    // public abstract DrawableV2 getDrawable();
    public DrawableV2 getDrawable() {
        return null;
    }

    /**
     * Get drawable data of this element on given area.
     * 
     * @param position
     * @param size
     * @return
     */
    public Drawable getPixels(Vector2d position, Size size) {
        if (cachedDrawable == null) {
            getPixels();
        }

        int[] pixels = new int[size.getWidth() * size.getHeight()];

        ImageArrayHelper.copyRectangle(//
                cachedDrawable.getPixels(), //
                cachedDrawable.getPixelSize(), //
                position, //
                size, //
                pixels, //
                size, //
                Vector2d.zero() //
        );

        return new DrawableImpl(pixels, size);
    }

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

    /**
     * 
     * @param checker
     */
    public abstract void visit(Visitor checker);

    /**
     * 
     * @param drawData
     * @param position
     * @param size
     */
    public void notifyRedraw(Drawable drawData, Vector2d position, Size size) {
        if (parent == null) {
            return;
        }

        getPixels();

        ImageArrayHelper.copyRectangle(//
                drawData, //
                Vector2d.zero(), //
                size, //
                cachedDrawable, //
                position //
        );

        parent.notifyRedraw(drawData, position.addNew(this.relativPosition), size);
    }

    public void redraw() {
        if (parent == null) {
            return;
        }

        parent.redraw();
    }

    public void notifyChange() {
        cachedDrawable = null;
        cachedDrawableV2 = null;
        if (parent == null) {
            return;
        }
        parent.notifyChange();
    }

}
