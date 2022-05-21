package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public interface WindowsMouseWheel {
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction);
}
