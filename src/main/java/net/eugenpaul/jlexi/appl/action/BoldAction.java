package net.eugenpaul.jlexi.appl.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractAction;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.button.ButtonState;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;

@AllArgsConstructor
public class BoldAction extends AbstractAction {
    private final String name;
    private final Button button;
    private final AbstractController controller;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.button.getState() == ButtonState.NORMAL) {
            this.button.setState(ButtonState.PUSHED);
            this.button.redraw();

            this.controller.propertyChange(new PropertyChangeEvent(//
                    this.name, //
                    ViewPropertyChangeType.CURSOR_SET_FORMAT_BOLD.getTypeName(), //
                    null, //
                    Boolean.TRUE //
            ));
        } else {
            this.button.setState(ButtonState.NORMAL);
            this.button.redraw();

            this.controller.propertyChange(new PropertyChangeEvent(//
                    this.name, //
                    ViewPropertyChangeType.CURSOR_SET_FORMAT_BOLD.getTypeName(), //
                    null, //
                    Boolean.FALSE //
            ));
        }
    }

}
