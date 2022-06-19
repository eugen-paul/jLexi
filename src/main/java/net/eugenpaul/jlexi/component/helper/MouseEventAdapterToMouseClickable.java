package net.eugenpaul.jlexi.component.helper;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class MouseEventAdapterToMouseClickable implements MouseEventAdapter {

    private MouseClickable target;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        target.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public MouseDraggable mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        return target.onMousePressed(mouseX, mouseY, button);
    }

    @Override
    public MouseDraggable mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        return target.onMouseReleased(mouseX, mouseY, button);
    }
}
