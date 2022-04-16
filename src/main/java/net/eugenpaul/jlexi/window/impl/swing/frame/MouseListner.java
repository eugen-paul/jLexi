package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class MouseListner extends MouseAdapter {

    private String name;
    private ModelController controller;

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.mouseClickOnWindow(name, e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.mousePressedOnWindow(name, e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.mouseReleasedOnWindow(name, e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }
}