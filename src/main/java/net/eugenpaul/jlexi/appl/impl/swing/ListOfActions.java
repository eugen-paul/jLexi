package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
@Data
public class ListOfActions {
    private List<KeyBindingAction> action;
}
