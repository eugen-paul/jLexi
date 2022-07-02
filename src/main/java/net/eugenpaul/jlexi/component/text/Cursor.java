package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.effect.SelectedEffect;

public class Cursor implements ModelPropertyChangeListner {

    private final String name;

    private TextElement textElement;

    @Getter
    private TextFormat textFormat;
    @Getter
    private TextFormatEffect textFormatEffect;

    private GlyphEffect cursorEffect;
    private AbstractController controller;

    // TODO: remove
    // Two selection Effects. Just for Tests
    private GlyphEffect selectedEffectFrom;
    private GlyphEffect selectedEffectTo;

    private TextElement selectionFrom;
    private TextElement selectionTo;

    public Cursor(TextElement glyphElement, AbstractController controller, String name) {
        this.name = name;
        this.textElement = glyphElement;
        this.cursorEffect = null;
        this.controller = controller;

        this.selectedEffectFrom = null;
        this.selectedEffectTo = null;

        this.selectionFrom = null;
        this.selectionTo = null;

        this.controller.addView(this);
    }

    public TextPosition getPosition() {
        return this.textElement.getTextPosition();
    }

    public boolean isTextSelected() {
        return this.selectionFrom != null && this.selectionTo != null;
    }

    public void removeSelection() {
        setTextSelection(null, null);
    }

    private void addSelectedEffect() {
        if (this.selectionFrom == null || this.selectionTo == null) {
            return;
        }

        removeSelectedEffect();

        this.selectedEffectFrom = new SelectedEffect(this.selectionFrom);
        this.controller.addEffectToController(this.selectedEffectFrom);

        this.selectedEffectTo = new SelectedEffect(this.selectionTo);
        this.controller.addEffectToController(this.selectedEffectTo);
    }

    private void removeSelectedEffect() {
        if (this.selectedEffectFrom == null || this.selectedEffectTo == null) {
            return;
        }

        this.selectionFrom.removeEffect(this.selectedEffectFrom);
        this.controller.removeEffectFromController(this.selectedEffectFrom);
        this.selectedEffectFrom = null;

        this.selectionTo.removeEffect(this.selectedEffectTo);
        this.controller.removeEffectFromController(this.selectedEffectTo);
        this.selectedEffectTo = null;
    }

    public void setTextSelection(TextElement from, TextElement to) {
        removeSelectedEffect();
        this.selectionFrom = from;
        this.selectionTo = to;
        addSelectedEffect();
    }

    public void moveCursorTo(TextPosition cursorPosition) {
        if (null != this.textElement && null != this.cursorEffect) {
            this.textElement.removeEffect(this.cursorEffect);
            this.controller.removeEffectFromController(this.cursorEffect);
        }

        if (null == cursorPosition) {
            return;
        }

        this.textElement = cursorPosition.getTextElement();
        this.textFormat = this.textElement.getFormat();
        this.textFormatEffect = this.textElement.getFormatEffect();

        this.cursorEffect = new CursorEffect(this.textElement);
        this.controller.addEffectToController(this.cursorEffect);

        this.controller.propertyChange(new PropertyChangeEvent(//
                name, //
                ViewPropertyChangeType.CURSOR_MOVE.getTypeName(), //
                null, //
                this.textElement //
        ));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (this.textFormat == null || !evt.getSource().equals(name)) {
            return;
        }

        if (evt.getPropertyName().equals(ViewPropertyChangeType.CURSOR_SET_FORMAT_BOLD.getTypeName()) //
        ) {
            this.textFormat = this.textFormat.withBold((Boolean) evt.getNewValue());
        } else if (evt.getPropertyName().equals(ViewPropertyChangeType.CURSOR_SET_FORMAT_ITALIC.getTypeName())) {
            this.textFormat = this.textFormat.withItalic((Boolean) evt.getNewValue());
        }
    }
}
