package net.eugenpaul.jlexi.draw;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Collection of Drawable Elements.
 */
public interface DrawableV2Sketch {

    /**
     * Add Drawable to Sketch at <code>(x,y)</code>.
     * 
     * @param drawable - drawable to add
     * @param x        - x position of added element
     * @param y        - y position of added element
     * @param z        - order of added element
     */
    public void addDrawable(DrawableV2 drawable, int x, int y, int z);

    /**
     * Add DrawableV2Sketch to Sketch at <code>(x,y)</code>.
     * 
     * @param sketch - sketch to add
     * @param x      - x position of added element
     * @param y      - y position of added element
     * @param z      - order of added element
     */
    public void addDrawableV2Sketch(DrawableV2Sketch sketch, int x, int y, int z);

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
    public default DrawableV2 draw() {
        return draw(getArea());
    }

    /**
     * generate Drawable element on area
     * 
     * @param area - area of generated element
     * @return Drawable element
     */
    public DrawableV2 draw(Area area);

    /**
     * return DrawableV2Sketch at area
     * 
     * @param area - area of generated element
     * @return DrawableV2Sketch element
     */
    public DrawableV2Sketch sketchAt(Area area);

    /**
     * Create copy of this Element
     * 
     * @return
     */
    public DrawableV2Sketch copy();

    /**
     * get Bounts of this Element
     * 
     * @return size
     */
    public Size getSize();

    /**
     * get Area of this Element
     * 
     * @return area
     */
    public Area getArea();

}
