package net.eugenpaul.jlexi.appl.impl.swing.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

@AllArgsConstructor
public class MouseListner extends MouseAdapter {

    private String name;
    private WindowController controller;

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controller.mouseWheelMoved(name, e.getX(), e.getY(), MouseWheelDirection.ofRotation(e.getWheelRotation()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.mouseDragged(name, e.getX(), e.getY(), MouseButton.ofButton(e.getButton()));
    }
}
