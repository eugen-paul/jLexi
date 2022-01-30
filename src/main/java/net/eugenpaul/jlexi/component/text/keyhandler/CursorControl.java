package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.Cursor;

public interface CursorControl {

    public boolean moveCursor(CursorMove move, Cursor cursor);
}
