package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public interface MouseClickListener {
    void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button);

    void mousePressed(Integer mouseX, Integer mouseY, MouseButton button);

    void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button);

    void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction);
}
