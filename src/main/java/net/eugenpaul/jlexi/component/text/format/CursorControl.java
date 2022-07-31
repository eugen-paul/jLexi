package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

public interface CursorControl {
    public TextPositionV2 move(TextPositionV2 position, MovePosition moving);

    public TextPosition getEol(TextPosition position);

    public TextPosition getBol(TextPosition position);

    public TextPosition getStart(TextPosition position);

    public TextPosition getEnd(TextPosition position);

    public TextPosition getPageUp(TextPosition position);

    public TextPosition getPageDown(TextPosition position);
}
