package net.eugenpaul.jlexi.component.text.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.text.CursorV2;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class TextPaneBoldActionV2 extends KeyBindingAction {

    private final CursorV2 cursor;

    @Override
    public void doAction() {
        cursor.switchBold();
    }

}
