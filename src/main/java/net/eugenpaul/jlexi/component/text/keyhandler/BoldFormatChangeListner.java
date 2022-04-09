package net.eugenpaul.jlexi.component.text.keyhandler;

import java.beans.PropertyChangeEvent;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.button.ButtonState;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;

@AllArgsConstructor
public class BoldFormatChangeListner implements ModelPropertyChangeListner {

    private TextButton button;
    private String srcName;

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getSource().equals(srcName)
                && evt.getPropertyName().equals(ViewPropertyChangeType.CURSOR_MOVE.getTypeName()) //
        ) {
            var element = (TextElement) evt.getNewValue();
            var format = element.getFormat();

            if (format.getBold()) {
                button.setState(ButtonState.PUSHED);
            } else {
                button.setState(ButtonState.NORMAL);
            }
            button.redraw();
        }
    }

}
