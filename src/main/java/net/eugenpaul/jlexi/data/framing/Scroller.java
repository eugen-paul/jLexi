package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;

/**
 * Add ScrollBar to Glyth
 */
public class Scroller extends MonoGlyph {

    /** hight of the scroller */
    private int hight;
    /** width of the scroller */
    private int width;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    protected Scroller(Glyph component, int width, int hight) {
        super(component);
        this.width = width;
        this.hight = hight;
    }

    @Override
    public GlyphIterator createIterator() {
        return null;
    }

    public void updateSize(int width, int hight) {
        // TODO alert Child
        this.width = width;
        this.hight = hight;
    }

}
