package net.eugenpaul.jlexi.controller;

import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.framing.MouseButton;

/**
 * Implementation of {@link AbstractController} for jLexi
 */
public class DefaultController extends AbstractController {

    /**
     * Window size was changed
     * 
     * @param size new size of window
     */
    public void resizeMainWindow(Size size) {
        setModelProperty(ModelPropertyChangeType.FORM_RESIZE, size);
    }

    /**
     * Mouse was clicked
     * 
     * @param mouseX Returns the horizontal x position of the event relative to the window component.
     * @param mouseY Returns the horizontal y position of the event relative to the window component.
     * @param button Returns which, if any, of the mouse buttons has changed state.
     */
    public void clickOnWindow(int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_CLICK, mouseX, mouseY, button);
    }
}
