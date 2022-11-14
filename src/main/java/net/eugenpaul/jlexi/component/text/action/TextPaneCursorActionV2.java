package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandlerV2;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneCursorActionV2 extends KeyBindingAction {

    private final AbstractKeyHandlerV2 keyHandler;

    private final MovePosition moving;

    @Override
    public void doAction() {
        keyHandler.moveCursor(moving);
    }

}
