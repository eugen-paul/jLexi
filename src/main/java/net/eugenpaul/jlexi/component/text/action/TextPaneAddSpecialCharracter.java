package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.component.text.keyhandler.SpecialCharacter;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneAddSpecialCharracter extends KeyBindingAction {

    private final AbstractKeyHandler keyHandler;
    private final SpecialCharacter sc;

    @Override
    public void doAction() {
        keyHandler.addSpecialCharacter(sc);
    }

}
