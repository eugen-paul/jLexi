package net.eugenpaul.jlexi.component;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.iterator.GlyphIterator;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;

@Getter
@Setter
public abstract class Glyph {

    private Glyph parent;
    private Verctor2d relativPosition;

    @Setter(lombok.AccessLevel.PROTECTED)
    private Size size;

    protected Glyph(Glyph parent) {
        this.parent = parent;
        this.relativPosition = new Verctor2d(0, 0);
        this.size = Size.ZERO_SIZE;
    }

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public abstract Drawable getPixels();

    /**
     * get a Iterator to iterate over children
     * 
     * @return Iterator
     */
    public abstract GlyphIterator createIterator();

    public abstract void visit(Visitor checker);

    public void notifyUpdate(Glyph child) {

    }
}
