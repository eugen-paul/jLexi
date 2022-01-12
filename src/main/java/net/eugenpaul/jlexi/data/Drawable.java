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
    public int[][] getPixels();

    /**
     * Get size and position of this Element.
     * @return
     */
    public Bounds getBounds();
}
