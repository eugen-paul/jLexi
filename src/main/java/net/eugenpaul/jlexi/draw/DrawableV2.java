package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Interface for immutable drawable Element.
 */
public interface DrawableV2 {

    /**
     * Get copy of the Element as ARBG Data.
     * 
     * @return ARBG Data
     */
    public int[] asArgbPixels();

    /**
     * Draw Element on given dest-Array at position
     * 
     * @param dest
     * @param destSize
     * @param position
     */
    public void toArgbPixels(int[] dest, Size destSize, Vector2d position);

    /**
     * Get copy of the Element as RBGA Data.
     * 
     * @return RBGA Data
     */
    public int[] asRgbaPixels();

    /**
     * Get copy of the Element as Color Data.
     * 
     * @return RBGA Data
     */
    public Color[] asColorPixels();

    /**
     * get Bounts of the Element
     * 
     * @return
     */
    public Size getSize();

}
