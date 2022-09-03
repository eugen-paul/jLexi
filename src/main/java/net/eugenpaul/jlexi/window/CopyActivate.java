package net.eugenpaul.jlexi.window;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class CopyActivate implements MouseEventAdapter {

    private final String name;
    private final ModelController controller;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        this.controller.copy(name);
    }
}
