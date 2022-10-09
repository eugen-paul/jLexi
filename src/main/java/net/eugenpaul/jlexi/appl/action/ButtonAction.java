package net.eugenpaul.jlexi.appl.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class ButtonAction implements MouseEventAdapter {

    private KeyBindingAction action;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        action.doAction();
    }
}
