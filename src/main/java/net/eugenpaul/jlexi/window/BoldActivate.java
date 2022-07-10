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
public class BoldActivate implements MouseEventAdapter {

    private final String name;
    private final Button button;
    private final AbstractController controller;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
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
