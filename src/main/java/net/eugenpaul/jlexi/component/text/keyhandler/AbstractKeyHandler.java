package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.EndOfLine;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public class AbstractKeyHandler {

    private KeyHandlerable component;

    protected AbstractKeyHandler(KeyHandlerable component) {
        this.component = component;
    }

    public void onKeyTyped(Character key) {
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }
        if (component.getMouseCursor() != null) {
            CharGlyph glyph = new CharGlyph(component.getThis(), key.charValue(), component.getFontStorage(), null);
            var node = component.getMouseCursor().getCurrentGlyph().insertBefore(glyph);
            glyph.setTextPaneListElement(node);
        }
        component.getParent().notifyUpdate(component.getThis());
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT, LEFT, UP, DOWN:
            keyPressedRight(keyCode);
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
        if (component.getMouseCursor() != null) {
            var elem = component.getMouseCursor().getCurrentGlyph().getPrev();
            if (elem != null) {
                elem.remove();
                component.getParent().notifyUpdate(component.getThis());
            }
        }
    }

    private void keyPressedDelete() {
        if (component.getMouseCursor() != null) {
            var elem = component.getMouseCursor().getCurrentGlyph();

            if (elem != null && !(elem.getData().isPlaceHolder())) {
                component.doCursorMove(CursorMove.RIGHT);
                elem.remove();
                component.getParent().notifyUpdate(component.getThis());
            }
        }
    }

    private void keyPressedRight(KeyCode keyCode) {
        component.doCursorMove(CursorMove.fromKeyCode(keyCode));
    }

    private void keyPressedEnter() {
        if (component.getMouseCursor() != null) {
            EndOfLine glyph = new EndOfLine(component.getThis(), component.getFontStorage(), null);
            var node = component.getMouseCursor().getCurrentGlyph().insertBefore(glyph);
            glyph.setTextPaneListElement(node);
            component.getParent().notifyUpdate(component.getThis());
        }
    }
}
