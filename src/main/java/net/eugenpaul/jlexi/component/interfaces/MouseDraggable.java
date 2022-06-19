package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseDraggable {
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button);
}
