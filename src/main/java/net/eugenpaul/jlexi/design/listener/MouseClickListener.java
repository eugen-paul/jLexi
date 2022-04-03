package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.MouseButton;

public interface MouseClickListener {
    public void mouseClicked(MouseButton button);

    public void mousePressed(MouseButton button);

    public void mouseReleased(MouseButton button);
}
