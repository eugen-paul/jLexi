package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneCursorAction extends KeyBindingAction {

    private final AbstractKeyHandler keyHandler;

    private final MovePosition moving;

    @Override
    public void doAction() {
        keyHandler.moveCursor(moving);
    }

}
