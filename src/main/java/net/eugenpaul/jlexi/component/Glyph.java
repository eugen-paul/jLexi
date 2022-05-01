package net.eugenpaul.jlexi.component;

import java.util.Iterator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2Sketch;
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
        this.cachedDrawableV2 = null;
    }

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public abstract DrawableV2 getDrawable();

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

    public void redraw() {
        this.cachedDrawableV2 = null;

        if (parent == null) {
            return;
        }

        parent.redraw();
    }

    public void notifyChange() {
        this.cachedDrawableV2 = null;

        if (parent == null) {
            return;
        }
        parent.notifyChange();
    }

}
