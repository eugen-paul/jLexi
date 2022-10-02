package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneRedoAction extends KeyBindingAction {

    private final AbstractKeyHandler keyHandler;

    @Override
    public void doAction() {
        keyHandler.redo();
    }

}
