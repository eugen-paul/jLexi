package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.KeyCode;

public interface KeyEventAdapter extends KeyPressListener {

    @Override
    default void keyTyped(Character key) {
    }

    @Override
    default void keyPressed(KeyCode keyCode) {
    }

    @Override
    default void keyReleased(KeyCode keyCode) {
    }
}
