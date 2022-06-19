package net.eugenpaul.jlexi.component.helper;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.interfaces.WindowsMouseWheel;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

@AllArgsConstructor
public class MouseEventAdapterToWindowsMouseWheel implements MouseEventAdapter {

    private WindowsMouseWheel target;

    @Override
    public void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        target.onMouseWhellMoved(mouseX, mouseY, direction);
    }
}
