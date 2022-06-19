package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public interface MouseEventAdapter extends MouseClickListener {
    @Override
    public default void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
    }

    @Override
    public default MouseDraggable mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        return null;
    }

    @Override
    public default MouseDraggable mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        return null;
    }

    @Override
    public default void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
    }
}
