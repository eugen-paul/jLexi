package net.eugenpaul.jlexi.appl.action;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.appl.subscriber.GlobalSubscribeTypes;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.button.ButtonState;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

@AllArgsConstructor
public class ButtonBoldAction implements MouseEventAdapter, EventSubscriber {

    private final Button button;
    private final KeyBindingAction action;
    private final String textCursorName;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        action.doAction();
    }

    @Override
    public void update(Object source, Object type, Object data) {
        if (!(source instanceof Cursor) //
                || !((Cursor) source).getName().equals(textCursorName) //
                || type != GlobalSubscribeTypes.TEXT_CURSOR_MOVE //
                || !(data instanceof TextElement) //
        ) {
            return;
        }

        var textElement = (TextElement) data;
        if (textElement.getFormat().getBold().booleanValue()) {
            this.button.setState(ButtonState.PUSHED);
        } else {
            this.button.setState(ButtonState.NORMAL);
        }
        this.button.redraw();
    }
}
