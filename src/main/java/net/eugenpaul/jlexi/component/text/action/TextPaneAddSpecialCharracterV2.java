package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandlerV2;
import net.eugenpaul.jlexi.component.text.keyhandler.SpecialCharacter;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneAddSpecialCharracterV2 extends KeyBindingAction {

    private final AbstractKeyHandlerV2 keyHandler;
    private final SpecialCharacter sc;

    @Override
    public void doAction() {
        keyHandler.addSpecialCharacter(sc);
    }

}
