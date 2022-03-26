package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.Size;

/**
 * Object that can receive resize notification from gui.
 */
public interface Resizeable {

    /**
     * Set new size
     * 
     * @param name - name of the object that will be resized
     * @param size - new size
     */
    public void resizeTo(String name, Size size);

    /**
     * Set new width und height
     * 
     * @param name   - name of the object that will be resized
     * @param width  - new width
     * @param height - new height
     */
    public default void resizeTo(String name, int width, int height) {
        resizeTo(name, new Size(width, height));
    }

}
