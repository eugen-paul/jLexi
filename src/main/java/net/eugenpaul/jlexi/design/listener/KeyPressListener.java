package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.KeyCode;

public interface KeyPressListener {
    void onKeyTyped(Character key);

    void onKeyPressed(KeyCode keyCode);

    void onKeyReleased(KeyCode keyCode);
}
