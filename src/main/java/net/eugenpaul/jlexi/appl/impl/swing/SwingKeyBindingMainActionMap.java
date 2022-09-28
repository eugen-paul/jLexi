package net.eugenpaul.jlexi.appl.impl.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;

public class SwingKeyBindingMainActionMap extends ActionMap {

    @Override
    public Action get(Object key) {
        if (!(key instanceof ListOfActions)) {
            return null;
        }

        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ListOfActions actions = (ListOfActions) key;

                for (var action : actions.getAction()) {
                    action.doAction();
                }
            }
        };
    }

}
