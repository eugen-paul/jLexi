package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.Size;

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
     * Set new width und height
     * 
     * @param width - new width
     * @param height - new height
     */
    public default void resizeTo(int width, int height) {
        resizeTo(new Size(width, height));
    }

}
