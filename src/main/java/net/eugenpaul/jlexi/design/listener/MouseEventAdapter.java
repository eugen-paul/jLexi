package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseEventAdapter extends MouseClickListener {
    @Override
    public default void mouseClicked(MouseButton button) {
    }

    @Override
    public default void mousePressed(MouseButton button) {
    }

    @Override
    public default void mouseReleased(MouseButton button) {
    }
}
