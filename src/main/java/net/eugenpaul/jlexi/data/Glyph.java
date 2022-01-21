package net.eugenpaul.jlexi.data;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

@Getter
@Setter
public abstract class Glyph {

    private Glyph parent;
    private Position relativPosition;

    @Setter(lombok.AccessLevel.PROTECTED)
    private Size preferredSize;

    protected Glyph(Glyph parent) {
        this.parent = parent;
        this.relativPosition = new Position(0, 0);
        this.preferredSize = Size.ZERO_SIZE;
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
}
