package net.eugenpaul.jlexi.design.listener;

import net.eugenpaul.jlexi.utils.event.KeyCode;

public interface KeyEventAdapter extends KeyPressListener {

    @Override
    default void onKeyTyped(Character key) {
    }

    @Override
    default void onKeyPressed(KeyCode keyCode) {
    }

    @Override
    default void onKeyReleased(KeyCode keyCode) {
    }
}
