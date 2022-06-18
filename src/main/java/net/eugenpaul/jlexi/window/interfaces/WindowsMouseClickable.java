package net.eugenpaul.jlexi.window.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface WindowsMouseClickable {
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button);
    public void onMousePressed(String name, Integer mouseX, Integer mouseY, MouseButton button);
    public void onMouseReleased(String name, Integer mouseX, Integer mouseY, MouseButton button);
    public void onMouseDragged(String name, Integer mouseX, Integer mouseY, MouseButton button);
}
