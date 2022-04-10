package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.Size;

/**
 * Object that can receive resize notification from gui.
 */
public interface Resizeable {

    boolean isResizeble();

    /**
     * Set new size
     * 
     * @param name - name of the object that will be resized
     * @param size - new size
     */
    void resizeTo(Size size);

    /**
     * Set new width und height
     * 
     * @param name   - name of the object that will be resized
     * @param width  - new width
     * @param height - new height
     */
    default void resizeTo(int width, int height) {
        resizeTo(new Size(width, height));
    }

}
