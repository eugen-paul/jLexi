package net.eugenpaul.jlexi.data;

import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

public interface Glyph {

    /**
     * Get drawable data of this element.
     * 
     * @return drawable data
     */
    public Drawable getPixels();

    /**
     * Get size of this Element.
     * 
     * @return size
     */
    public Size getSize();

    /**
     * Check whether the point lies on the element.
     * 
     * @return true - the Point is on Element.
     */
    public boolean isIntersects(Point point);

    /**
     * Insert a Glyph to this Glyph on given position
     * 
     * @param glyph    Glyph that will be added.
     * @param position Position where the glyph is added.
     */
    public void insert(Glyph glyph, int position);

    /**
     * Remove Glyph to this Glyph
     * 
     * @param glyph Glyph that will be removed
     */
    public void remove(Glyph glyph);

    /**
     * Get Glyph on position
     * 
     * @param position position of the glyph
     * @return Glyph
     */
    public Glyph child(int position);

    /**
     * get a Iterator to iterate over children
     * 
     * @return Iterator
     */
    public GlyphIterator createIterator();

    /**
     * Get Parant of this Glyph
     * 
     * @return
     */
    public Glyph parent();

    public void visit(Visitor checker);
}
