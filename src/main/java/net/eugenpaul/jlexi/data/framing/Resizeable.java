package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Size;

/**
 * Object that can receive resize notification from gui.
 */
public interface Resizeable {

    /**
     * Set new size
     * 
     * @param size - new size
     */
    public void setSize(Size size);

    /**
     * Set new width und hight
     * 
     * @param width - new width
     * @param hight - new hight
     */
    public default void setSize(int width, int hight) {
        setSize(new Size(width, hight));
    }

}
