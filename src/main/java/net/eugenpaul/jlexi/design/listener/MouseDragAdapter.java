package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseDragAdapter extends MouseDragListener {
    @Override
    public default void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
    }
}
