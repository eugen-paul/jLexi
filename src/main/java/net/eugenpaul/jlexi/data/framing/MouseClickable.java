package net.eugenpaul.jlexi.data.framing;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface MouseClickable {
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button);
}
