package net.eugenpaul.jlexi.component;

import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Getter
@Setter
public abstract class Glyph {

    protected static final NullIterator<Glyph> NULLITERATOR = new NullIterator<>();

    protected Glyph parent;
    protected Vector2d relativPosition;

    @Setter(lombok.AccessLevel.PROTECTED)
    protected Size size;

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
    public abstract Drawable getPixels(Vector2d position, Size size);

    /**
     * get a Iterator to iterate over children
     * 
     * @return Iterator
     */
    public abstract Iterator<Glyph> iterator();

    public Vector2d getRelativPositionTo(Glyph glyph) {
        Glyph parentGlyph = parent;
        Vector2d responsePosition = new Vector2d(relativPosition);
        while (null != parentGlyph) {
            if (parentGlyph == glyph) {
                break;
            }
            responsePosition.add(parentGlyph.getRelativPosition());
            // responsePosition
            parentGlyph = parentGlyph.getParent();
        }

        if (null == parentGlyph) {
            return null;
        }
        return responsePosition;
    }

    public abstract void visit(Visitor checker);

    public abstract void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size);
}
