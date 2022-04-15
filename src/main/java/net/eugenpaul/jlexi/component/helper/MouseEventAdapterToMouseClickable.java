package net.eugenpaul.jlexi.component.helper;

import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;

public class MouseEventAdapterToMouseClickable implements MouseEventAdapter {

    private MouseClickable target;

    public MouseEventAdapterToMouseClickable(MouseClickable target) {
        this.target = target;
    }

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        target.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public void mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        target.onMousePressed(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        target.onMouseReleased(mouseX, mouseY, button);
    }
}
