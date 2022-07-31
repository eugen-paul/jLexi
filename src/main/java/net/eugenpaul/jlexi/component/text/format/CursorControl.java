package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

public interface CursorControl {
    public TextPositionV2 move(TextPositionV2 position, MovePosition moving);
}
