package net.eugenpaul.jlexi.component;

import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

@Getter
@Setter
public abstract class Glyph {

    protected static final NullIterator<Glyph> NULLITERATOR = new NullIterator<>();

    protected Glyph parent;
    protected Vector2d relativPosition;

    protected Drawable cachedDrawable;

    protected Size size;

    /**
     * C'tor
     * 
     * @param parent
     */
    protected Glyph(Glyph parent) {
        this.parent = parent;
        this.relativPosition = new Vector2d(0, 0);
        this.size = Size.ZERO_SIZE;
    }

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public abstract Drawable getPixels();

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

        if (cachedDrawable == null) {
            getPixels();
        }

        ImageArrayHelper.copyRectangle(//
                drawData, //
                Vector2d.zero(), //
                size, //
                cachedDrawable, //
                position //
        );

        parent.notifyRedraw(drawData, position.addNew(this.relativPosition), size);
    }
}
