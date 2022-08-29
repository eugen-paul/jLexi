package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseDragListener {
    void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button);
}
