package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

public interface CursorControl {
    public TextPosition getNext(TextPosition position);

    public TextPosition getPrevious(TextPosition position);

    public TextPosition getUp(TextPosition position);
    public TextPositionV2 getUp(TextPositionV2 position);

    public TextPosition getDown(TextPosition position);
    public TextPositionV2 getDown(TextPositionV2 position);

    public TextPosition getEol(TextPosition position);

    public TextPosition getBol(TextPosition position);

    public TextPosition getStart(TextPosition position);

    public TextPosition getEnd(TextPosition position);

    public TextPosition getPageUp(TextPosition position);

    public TextPosition getPageDown(TextPosition position);
}
