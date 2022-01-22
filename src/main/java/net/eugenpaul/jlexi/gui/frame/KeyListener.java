package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.DefaultController;

@AllArgsConstructor
public class KeyListener extends KeyAdapter {

    private DefaultController controller;

    @Override
    public void keyPressed(KeyEvent e) {
        controller.keyPressed(e.getKeyChar());
    }
}
