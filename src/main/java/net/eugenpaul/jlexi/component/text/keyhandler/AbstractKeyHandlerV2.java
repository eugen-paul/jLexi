package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.event.KeyCode;

public class AbstractKeyHandlerV2 {

    private KeyHandlerableV2 component;
    private FontStorage fontStorage;

    protected AbstractKeyHandlerV2(KeyHandlerableV2 component, FontStorage fontStorage) {
        this.component = component;
        this.fontStorage = fontStorage;
    }

    public void onKeyTyped(Character key) {
        var cursor = component.getMouseCursor();
        var element = cursor.getCurrentGlyph();
        var textField = element.getParentTextField();

        if (null == textField) {
            return;
        }

        textField.addBefore(element, new TextChar(null, fontStorage, textField, key));
        textField.notifyChange();
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT, LEFT, UP, DOWN:
            keyPressedCursorMove(keyCode);
            break;
        case DELETE:
            keyPressedDelete();
            break;
        case BACK_SPACE:
            keyPressedBackSpace();
            break;
        default:
            break;
        }
    }

    public void onKeyReleased(KeyCode keyCode) {
        // Nothing to do
    }

    private void keyPressedBackSpace() {
        // TODO
    }

    private void keyPressedDelete() {
        // TODO
    }

    private void keyPressedCursorMove(KeyCode keyCode) {
        var cursor = component.getMouseCursor();

        var structureForm = component.getTextStructureForm();

        if (null == structureForm) {
            return;
        }

        TextElement el = null;
        switch (keyCode) {
        case RIGHT:
            el = structureForm.getNext(cursor.getCurrentGlyph());
            break;
        case LEFT:
            el = structureForm.getPrevious(cursor.getCurrentGlyph());
            break;
        case UP:
            el = structureForm.getUp(cursor.getCurrentGlyph());
            break;
        case DOWN:
            el = structureForm.getDown(cursor.getCurrentGlyph());
            break;
        default:
            break;
        }

        if (el == null) {
            return;
        }

        cursor.moveCursorTo(el);
    }

    private void keyPressedEnter() {
        // TODO
    }
}
