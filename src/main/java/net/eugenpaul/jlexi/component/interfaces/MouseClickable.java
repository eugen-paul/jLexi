package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface MouseClickable {
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button);

    public MouseDraggable onMousePressed(Integer mouseX, Integer mouseY, MouseButton button);

    public MouseDraggable onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button);
}
