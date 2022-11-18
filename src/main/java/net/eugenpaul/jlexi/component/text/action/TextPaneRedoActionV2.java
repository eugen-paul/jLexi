package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandlerV2;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneRedoActionV2 extends KeyBindingAction {

    private final AbstractKeyHandlerV2 keyHandler;

    @Override
    public void doAction() {
        keyHandler.redo();
    }

}
