package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

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
