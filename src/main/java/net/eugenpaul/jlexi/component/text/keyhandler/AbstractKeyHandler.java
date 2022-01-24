package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.EndOfLine;
import net.eugenpaul.jlexi.effect.CursorEffect;
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
        if (component.getCursor() != null) {
            CharGlyph glyph = new CharGlyph(component.getThis(), key.charValue(), component.getFontStorage(), null);
            var node = component.getCursor().getGlyph().getTextPaneListElement().insertBefore(glyph);
            glyph.setTextPaneListElement(node);
        }
        component.getParent().notifyUpdate(component.getThis());
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT:
            keyPressedRight();
            break;
        case LEFT:
            keyPressedLeft();
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

    public void onKeyReleased(KeyCode keyCode){
        //Nothing to do
    }

    private void keyPressedBackSpace() {
        if (component.getCursor() != null) {
            var elem = component.getCursor().getGlyph().getTextPaneListElement().getPrev();
            if (elem != null) {
                elem.remove();
                component.getParent().notifyUpdate(component.getThis());
            }
        }
    }

    private void keyPressedDelete() {
        if (component.getCursor() != null) {
            var elem = component.getCursor().getGlyph().getTextPaneListElement();
            if (elem != null && !(elem.getData().isPlaceHolder())) {
                component.getEffectHandler().removeEffect(component.getCursor());
                if (elem.getNext() != null) {
                    component.setCursor(new CursorEffect(elem.getNext().getData()));
                    elem.getNext().getData().addEffect(component.getCursor());
                } else if (elem.getPrev() != null) {
                    component.setCursor(new CursorEffect(elem.getPrev().getData()));
                    elem.getPrev().getData().addEffect(component.getCursor());
                }
                elem.remove();
                component.getEffectHandler().addEffect(component.getCursor());
                component.getParent().notifyUpdate(component.getThis());
            }
        }
    }

    private void keyPressedLeft() {
        if (component.getCursor() != null) {
            var elem = component.getCursor().getGlyph().getTextPaneListElement().getPrev();
            if (elem != null) {
                component.getEffectHandler().removeEffect(component.getCursor());
                component.setCursor(new CursorEffect(elem.getData()));
                elem.getData().addEffect(component.getCursor());
                component.getEffectHandler().addEffect(component.getCursor());
            }
        }
    }

    private void keyPressedRight() {
        if (component.getCursor() != null) {
            var elem = component.getCursor().getGlyph().getTextPaneListElement().getNext();
            if (elem != null) {
                component.getEffectHandler().removeEffect(component.getCursor());
                component.setCursor(new CursorEffect(elem.getData()));
                elem.getData().addEffect(component.getCursor());
                component.getEffectHandler().addEffect(component.getCursor());
            }
        }
    }

    private void keyPressedEnter() {
        if (component.getCursor() != null) {
            EndOfLine glyph = new EndOfLine(component.getThis(), component.getFontStorage(), null);
            var node = component.getCursor().getGlyph().getTextPaneListElement().insertBefore(glyph);
            glyph.setTextPaneListElement(node);
            component.getParent().notifyUpdate(component.getThis());
        }
    }
}
