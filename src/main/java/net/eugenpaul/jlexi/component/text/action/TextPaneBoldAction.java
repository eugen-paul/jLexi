package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneBoldAction extends KeyBindingAction {

    private final Cursor cursor;

    @Override
    public void doAction() {
        cursor.switchBold();
    }

}
