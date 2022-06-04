package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.Size;

/**
 * Object that can receive resize notification from gui.
 */
public interface Resizeable {

    boolean isResizeble();

    /**
     * The function tries to change the size of the element. The element can decide for itself whether the size is taken
     * over or adjusted on the basis of the passed size.
     * 
     * @param name - name of the object that will be resized
     * @param size - new size
     */
    void resizeTo(Size size);

    /**
     * The function tries to change the size of the element. The element can decide for itself whether the size is taken
     * over or adjusted on the basis of the passed size.
     * 
     * @param name   - name of the object that will be resized
     * @param width  - new width
     * @param height - new height
     */
    default void resizeTo(int width, int height) {
        resizeTo(new Size(width, height));
    }

}
