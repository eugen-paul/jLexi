package net.eugenpaul.jlexi.appl.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class UndoActivate implements MouseEventAdapter {

    private final String name;
    private final ModelController controller;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        this.controller.undo(name);
    }
}
