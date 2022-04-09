package net.eugenpaul.jlexi.window.interfaces;

import net.eugenpaul.jlexi.utils.event.KeyCode;

/**
 * Object that can receive KeyPressable notification from gui.
 */
public interface WindowsKeyPressable {
    public void onKeyTyped(String name, Character key);

    public void onKeyPressed(String name, KeyCode keyCode);

    public void onKeyReleased(String name, KeyCode keyCode);
}
