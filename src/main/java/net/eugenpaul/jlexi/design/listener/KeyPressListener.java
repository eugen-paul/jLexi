package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.KeyCode;

public interface KeyPressListener {
    void keyTyped(Character key);

    void keyPressed(KeyCode keyCode);

    void keyReleased(KeyCode keyCode);
}
