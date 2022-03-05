package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Size;

/**
 * Interface for drawable Elements.
 */
public interface Drawable {

    /**
     * Gets RBG Data of the Element.
     * 
     * @return RBG Data
     */
    public int[] getPixels();

    /**
     * get Bounts of the Element
     * 
     * @return
     */
    public Size getPixelSize();

    /**
     * get Copy of the Element
     * @return
     */
    public Drawable copy();
}
