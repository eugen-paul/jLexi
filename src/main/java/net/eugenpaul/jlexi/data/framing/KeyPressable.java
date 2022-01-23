package net.eugenpaul.jlexi.data.framing;

/**
 * Object that can receive KeyPressable notification from gui.
 */
public interface KeyPressable {
    public void onKeyTyped(Character key);
    public void onKeyPressed(KeyCode keyCode);
    public void onKeyReleased(KeyCode keyCode);
}
