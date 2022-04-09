package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseClickListener {
    void mouseClicked(MouseButton button);

    void mousePressed(MouseButton button);

    void mouseReleased(MouseButton button);
}
