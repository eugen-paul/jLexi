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
    public void resizeTo(Size size);

    /**
     * Set new width und hight
     * 
     * @param width - new width
     * @param hight - new hight
     */
    public default void resizeTo(int width, int hight) {
        resizeTo(new Size(width, hight));
    }

}
