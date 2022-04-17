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

public class Cursor implements ModelPropertyChangeListner {

    private final String name;

    private TextElement textElement;

    @Getter
    private TextFormat textFormat;
    @Getter
    private TextFormatEffect textFormatEffect;

    private GlyphEffect effect;
    private AbstractController controller;

    public Cursor(TextElement glyphElement, GlyphEffect effect, AbstractController controller, String name) {
        this.name = name;
        this.textElement = glyphElement;
        this.effect = effect;
        this.controller = controller;

        this.controller.addView(this);
    }

    public TextPosition getPosition() {
        return textElement.getTextPosition();
    }

    public void moveCursorTo(TextPosition cursorPosition) {
        if (null != this.textElement && null != effect) {
            this.textElement.removeEffect(effect);
            this.controller.removeEffectFromController(effect);
        }

        if (null == cursorPosition) {
            return;
        }

        this.textElement = cursorPosition.getTextElement();
        this.textFormat = this.textElement.getFormat();
        this.textFormatEffect = this.textElement.getFormatEffect();

        this.effect = new CursorEffect(this.textElement);
        this.controller.addEffectToController(effect);

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
