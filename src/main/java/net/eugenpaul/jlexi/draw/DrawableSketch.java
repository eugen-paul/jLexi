package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Collection of Drawable Elements.
 */
public interface DrawableSketch {

    /**
     * Add Drawable to Sketch at <code>(x,y)</code>.
     * 
     * @param drawable - drawable to add
     * @param x        - x position of added element
     * @param y        - y position of added element
     * @param z        - order of added element
     */
    void addDrawable(Drawable drawable, int x, int y, int z);

    /**
     * Add Drawable to Sketch at <code>(x,y)</code> with z = 0.
     * 
     * @param drawable - drawable to add
     * @param x        - x position of added element
     * @param y        - y position of added element
     */
    default void addDrawable(Drawable drawable, int x, int y) {
        addDrawable(drawable, x, y, 0);
    }

    /**
     * set default Background
     * 
     * @param color
     */
    void setBackground(Color color);

    /**
     * get default Background
     * 
     * @return Background
     */
    Color getBackground();

    /**
     * generate complete Drawable element
     * 
     * @return complete Drawable element
     */
    Drawable draw();

    /**
     * generate Drawable element on area
     * 
     * @param area - area of generated element
     * @return Drawable element
     */
    Drawable draw(Area area);

    /**
     * return DrawableSketch at area
     * 
     * @param area - area of generated element
     * @return DrawableSketch element
     */
    DrawableSketch sketchAt(Area area);

    /**
     * Create copy of this Element
     * 
     * @return
     */
    DrawableSketch copy();

    /**
     * get Bounts of this Element
     * 
     * @return size
     */
    Size getSize();

}
