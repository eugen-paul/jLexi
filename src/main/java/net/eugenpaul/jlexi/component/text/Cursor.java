package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;

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

    private List<TextElement> selectedText;
    private List<GlyphEffect> selectedTextEffect;

    public Cursor(TextElement glyphElement, AbstractController controller, String name) {
        this.name = name;
        this.textElement = glyphElement;
        this.cursorEffect = null;
        this.controller = controller;

        this.selectedText = null;
        this.selectedTextEffect = null;

        this.controller.addView(this);
    }

    public TextPosition getPosition() {
        return this.textElement.getTextPosition();
    }

    public boolean isTextSelected() {
        return this.selectedText != null && !this.selectedText.isEmpty();
    }

    public void removeSelection() {
        setTextSelection(null);
    }

    private void addSelectedEffect() {
        if (!isTextSelected()) {
            return;
        }

        removeSelectedEffect();

        var iteratorText = this.selectedText.iterator();

        this.selectedTextEffect = new LinkedList<>();

        while (iteratorText.hasNext()) {
            var textElem = iteratorText.next();

            var effect = new SelectedEffect(textElem);
            this.controller.addEffectToController(effect);
            selectedTextEffect.add(effect);
        }
    }

    private void removeSelectedEffect() {
        if (this.selectedTextEffect == null || this.selectedTextEffect.isEmpty()) {
            // nothing to remove
            return;
        }

        var iteratorText = this.selectedText.iterator();
        var iteratorTextEffect = this.selectedTextEffect.iterator();

        while (iteratorText.hasNext() && iteratorTextEffect.hasNext()) {
            var textElem = iteratorText.next();
            var textElemEffect = iteratorTextEffect.next();

            textElem.removeEffect(textElemEffect);
            this.controller.removeEffectFromController(textElemEffect);
        }

        this.selectedText = null;
        this.selectedTextEffect = null;
    }

    public void setTextSelection(List<TextElement> selection) {
        removeSelectedEffect();
        this.selectedText = selection;
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
