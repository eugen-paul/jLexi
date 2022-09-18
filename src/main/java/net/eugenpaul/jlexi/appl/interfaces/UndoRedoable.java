package net.eugenpaul.jlexi.appl.interfaces;

/**
 * Object that can receive undo/redo notification from gui.
 */
public interface UndoRedoable {
    public void undo(String name);

    public void redo(String name);
}
