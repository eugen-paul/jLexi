package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public interface CursorControl {
    public TextPosition move(TextPosition position, MovePosition moving);
}
