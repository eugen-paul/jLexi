package net.eugenpaul.jlexi.data;

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
}
