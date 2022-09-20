package net.eugenpaul.jlexi.window.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeyBindingActionSwing extends KeyBindingAction {

    @AllArgsConstructor
    private final class KBAction extends AbstractAction {

        private transient KeyBindingActionSwing parent;

        @Override
        public void actionPerformed(ActionEvent e) {
            parent.doAction();
        }
    }

    public KeyBindingActionSwing() {
        this.action = new KBAction(this);
    }

    @Override
    public void doAction() {
        LOGGER.debug("KeyBindingActionSwing.doAction()");
    }

}
