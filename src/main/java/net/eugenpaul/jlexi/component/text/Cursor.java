package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.GlyphEffect;

public class Cursor implements ModelPropertyChangeListner {

    private final String name;

    @Getter
    private TextElement textElement;

    @Getter
    private TextFormat textFormat;
    private GlyphEffect effect;
    private AbstractController controller;

    public Cursor(TextElement glyphElement, GlyphEffect effect, AbstractController controller, String name) {
        this.name = name;
        this.textElement = glyphElement;
        this.effect = effect;
        this.controller = controller;

        this.controller.addView(this);
    }

    public void moveCursorTo(TextElement textElement) {
        if (null != this.textElement && null != effect) {
            this.textElement.removeEffect(effect);
            controller.removeEffectFromController(effect);
        }

        if (null == textElement) {
            return;
        }

        this.textElement = textElement;
        this.textFormat = textElement.getFormat();
        effect = new CursorEffect(textElement);
        controller.addEffectToController(effect);

        controller.propertyChange(new PropertyChangeEvent(//
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
