package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseClickListener {
    void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button);

    void mousePressed(Integer mouseX, Integer mouseY, MouseButton button);

    void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button);
}
