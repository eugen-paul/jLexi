package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.framing.KeyCode;

@AllArgsConstructor
public class KeyListener extends KeyAdapter {

    private DefaultController controller;

    @Override
    public void keyPressed(KeyEvent e) {
        KeyCode code = keyToKeyCode(e);
        if (null != code) {
            controller.keyPressed(code);
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
        default:
            break;
        }
        return code;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KeyCode code = keyToKeyCode(e);
        if (null != code) {
            controller.keyReleased(code);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        controller.keyTyped(e.getKeyChar());
    }
}
