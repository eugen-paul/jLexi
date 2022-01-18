package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.framing.MouseButton;

@AllArgsConstructor
public class MouseListner extends MouseAdapter {

    private DefaultController controller;

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.clickOnWindow(e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }
}
