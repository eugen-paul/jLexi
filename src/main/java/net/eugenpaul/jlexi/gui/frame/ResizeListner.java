package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.AbstractController;

@AllArgsConstructor
public class ResizeListner extends ComponentAdapter {

    private AbstractController controller;

    @Override
    public void componentResized(ComponentEvent e) {
        System.out.print(((JPanel) e.getSource()).getWidth() + " ");
        System.out.println(((JPanel) e.getSource()).getHeight());
    }
}
