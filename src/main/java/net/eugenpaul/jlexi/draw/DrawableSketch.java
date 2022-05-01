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
    public void addDrawable(Drawable drawable, int x, int y, int z);

    /**
     * Add Drawable to Sketch at <code>(x,y)</code> with z = 0.
     * 
     * @param drawable - drawable to add
     * @param x        - x position of added element
     * @param y        - y position of added element
     */
    public default void addDrawable(Drawable drawable, int x, int y) {
        addDrawable(drawable, x, y, 0);
    }

    /**
     * set default Background
     * 
     * @param color
     */
    public void setBackground(Color color);

    /**
     * get default Background
     * 
     * @return Background
     */
    public Color getBackground();

    /**
     * generate complete Drawable element
     * 
     * @return complete Drawable element
     */
    public Drawable draw();

    /**
     * generate Drawable element on area
     * 
     * @param area - area of generated element
     * @return Drawable element
     */
    public Drawable draw(Area area);

    /**
     * return DrawableSketch at area
     * 
     * @param area - area of generated element
     * @return DrawableSketch element
     */
    public DrawableSketch sketchAt(Area area);

    /**
     * Create copy of this Element
     * 
     * @return
     */
    public DrawableSketch copy();

    /**
     * get Bounts of this Element
     * 
     * @return size
     */
    public Size getSize();

}
