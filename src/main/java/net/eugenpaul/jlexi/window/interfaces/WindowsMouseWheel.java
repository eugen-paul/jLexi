package net.eugenpaul.jlexi.window.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

/**
 * Object that can receive mouseWhellMoved notification from gui.
 */
public interface WindowsMouseWheel {
    public void onMouseWheelMooved(String name, Integer mouseX, Integer mouseY, MouseWheelDirection direction);
}
