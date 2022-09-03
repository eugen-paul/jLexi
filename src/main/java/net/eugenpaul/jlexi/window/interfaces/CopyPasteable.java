package net.eugenpaul.jlexi.window.interfaces;

/**
 * Object that can receive undo/redo notification from gui.
 */
public interface CopyPasteable {
    public void copy(String name);

    public void paste(String name);
}
