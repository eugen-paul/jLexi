package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Interface for immutable drawable Element.
 */
public interface Drawable {

    /**
     * Get copy of the Element as ARBG Data.
     * 
     * @return ARBG Data
     */
    public int[] asArgbPixels();

    /**
     * Draw Element on given dest-Array an given drawArea and position
     * 
     * @param dest
     * @param destSize
     * @param drawArea
     * @param relativePos - relativ to drawArea
     */
    public void toArgbPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos);

    /**
     * Get copy of the Element as RBGA Data.
     * 
     * @return RBGA Data
     */
    public int[] asRgbaPixels();

    /**
     * Draw Element on given dest-Array an given drawArea and position
     * 
     * @param dest
     * @param destSize
     * @param drawArea
     * @param relativePos - relativ to drawArea
     */
    public void toRgbaPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos);

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
