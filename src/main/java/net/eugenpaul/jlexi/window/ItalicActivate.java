package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.button.ButtonState;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;

@AllArgsConstructor
public class ItalicActivate implements MouseEventAdapter {

    private String name;
    private Button button;
    private AbstractController controller;

    @Override
    public void mouseClicked(MouseButton button) {
        if (this.button.getState() == ButtonState.NORMAL) {
            this.button.setState(ButtonState.PUSHED);
            this.button.redraw();

            controller.propertyChange(new PropertyChangeEvent(//
                    name, //
                    ViewPropertyChangeType.CURSOR_SET_FORMAT_ITALIC.getTypeName(), //
                    null, //
                    Boolean.TRUE //
            ));
        } else {
            this.button.setState(ButtonState.NORMAL);
            this.button.redraw();

            controller.propertyChange(new PropertyChangeEvent(//
                    name, //
                    ViewPropertyChangeType.CURSOR_SET_FORMAT_ITALIC.getTypeName(), //
                    null, //
                    Boolean.FALSE //
            ));
        }
    }
}
