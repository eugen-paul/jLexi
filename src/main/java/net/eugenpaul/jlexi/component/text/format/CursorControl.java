package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.interfaces.CursorPosition;

public interface CursorControl {
    public CursorPosition getNext(CursorPosition position);

    public CursorPosition getPrevious(CursorPosition position);

    public CursorPosition getUp(CursorPosition position);

    public CursorPosition getDown(CursorPosition position);

    public CursorPosition getEol(CursorPosition position);

    public CursorPosition getBol(CursorPosition position);

    public CursorPosition getStart(CursorPosition position);

    public CursorPosition getEnd(CursorPosition position);

    public CursorPosition getPageUp(CursorPosition position);

    public CursorPosition getPageDown(CursorPosition position);
}
