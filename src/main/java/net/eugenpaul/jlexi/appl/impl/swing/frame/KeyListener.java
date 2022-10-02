package net.eugenpaul.jlexi.appl.impl.swing.frame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.event.KeyCode;

@AllArgsConstructor
public class KeyListener extends KeyAdapter {

    private String name;
    private ModelController controller;

    @Override
    public void keyPressed(KeyEvent e) {
        KeyCode code = keyToKeyCode(e);
        if (null != code) {
            controller.keyPressed(name, code);
        }
    }

    private KeyCode keyToKeyCode(KeyEvent e) {
        KeyCode code = null;
        switch (e.getKeyCode()) {
        case KeyEvent.VK_SHIFT:
            code = KeyCode.SHIFT;
            break;
        case KeyEvent.VK_ALT:
            code = KeyCode.ALT;
            break;
        case KeyEvent.VK_CONTROL:
            code = KeyCode.CTRL;
            break;
        case KeyEvent.VK_UP:
            code = KeyCode.UP;
            break;
        case KeyEvent.VK_DOWN:
            code = KeyCode.DOWN;
            break;
        case KeyEvent.VK_LEFT:
            code = KeyCode.LEFT;
            break;
        case KeyEvent.VK_RIGHT:
            code = KeyCode.RIGHT;
            break;
        case KeyEvent.VK_DELETE:
            code = KeyCode.DELETE;
            break;
        case KeyEvent.VK_BACK_SPACE:
            code = KeyCode.BACK_SPACE;
            break;
        case KeyEvent.VK_ENTER:
            code = KeyCode.ENTER;
            break;
        case KeyEvent.VK_F1:
            code = KeyCode.F1;
            break;
        case KeyEvent.VK_F2:
            code = KeyCode.F2;
            break;
        case KeyEvent.VK_F3:
            code = KeyCode.F3;
            break;
        case KeyEvent.VK_F4:
            code = KeyCode.F4;
            break;
        default:
            break;
        }
        return code;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KeyCode code = keyToKeyCode(e);
        if (null != code) {
            controller.keyReleased(name, code);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        controller.keyTyped(name, e.getKeyChar());
    }
}
