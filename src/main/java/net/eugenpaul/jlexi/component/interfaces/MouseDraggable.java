package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseDraggable {
    /**
     * Callback for MouseDrag.
     * 
     * @param mouseX - Absolute x-position of the cursor
     * @param mouseY - Absolute y-position of the cursor
     * @param button
     */
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button);
}
