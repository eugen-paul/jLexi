package net.eugenpaul.jlexi.appl.impl.swing;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SwingKeyBindingMainActionMap extends ActionMap {

    private transient ExecutorService pool;

    @Override
    public Action get(Object key) {
        if (!(key instanceof ListOfActions)) {
            return null;
        }

        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pool.execute(() -> {
                    final ListOfActions actions = (ListOfActions) key;

                    for (var action : actions.getAction()) {
                        action.doAction();
                    }
                });
            }
        };
    }

}
