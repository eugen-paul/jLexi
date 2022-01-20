package net.eugenpaul.jlexi.data;

public interface GlyphObserver {

    /**
     * This method is called whenever the child glyph object is changed.
     * 
     * @param glyph changed child glyph.
     */
    public void updateChild(Glyph glyph);
}
