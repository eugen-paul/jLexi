package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface MouseClickable {
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button);
}
