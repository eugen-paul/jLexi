package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class MouseListner extends MouseAdapter {

    private ModelController controller;

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.clickOnWindow(e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }
}
