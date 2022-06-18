package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public interface MouseEventAdapter extends MouseClickListener {
    @Override
    public default void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
    }

    @Override
    public default void mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
    }

    @Override
    public default void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
    }

    @Override
    public default void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
    }

    @Override
    public default void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
    }
}
