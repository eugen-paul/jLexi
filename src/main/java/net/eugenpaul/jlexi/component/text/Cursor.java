package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.command.TextElementChangeFormatCommand;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.keyhandler.TextCommandsDeque;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.effect.SelectedEffect;

public class Cursor implements ModelPropertyChangeListner {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Cursor.class);

    private final String name;
    private final TextCommandsDeque commandDeque;

    private TextElement textElement;

    @Getter
    private TextFormat textFormat;
    @Getter
    private TextFormatEffect textFormatEffect;

    private GlyphEffect cursorEffect;
    private AbstractController controller;

    private List<TextElement> selectedText;
    private GlyphEffect selectedTextEffect;

    public Cursor(TextElement glyphElement, AbstractController controller, String name,
            TextCommandsDeque commandDeque) {
        this.name = name;
        this.commandDeque = commandDeque;
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

        this.selectedTextEffect = new SelectedEffect(this.selectedText);
        this.controller.addEffectToController(selectedTextEffect);
    }

    private void removeSelectedEffect() {
        if (this.selectedTextEffect == null) {
            // nothing to remove
            return;
        }

        for (var element : this.selectedText) {
            element.removeEffect(selectedTextEffect);
        }

        this.controller.removeEffectFromController(selectedTextEffect);

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
                this.name, //
                ViewPropertyChangeType.CURSOR_MOVE.getTypeName(), //
                null, //
                this.textElement //
        ));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (this.textFormat == null || !evt.getSource().equals(this.name)) {
            return;
        }

        ViewPropertyChangeType type = ViewPropertyChangeType.fromValue(evt.getPropertyName());
        if (type == null) {
            return;
        }

        switch (type) {
        case CURSOR_SET_FORMAT_BOLD:
            setBold((Boolean) evt.getNewValue());
            break;
        case CURSOR_SET_FORMAT_ITALIC:
            setItalic((Boolean) evt.getNewValue());
            break;
        default:
            break;
        }
    }

    private void setBold(Boolean isBold) {
        this.textFormat = this.textFormat.withBold(isBold);

        if (!isTextSelected()) {
            return;
        }

        List<TextFormat> newFormatList = this.selectedText.stream()//
                .map(v -> v.getFormat().withBold(isBold))//
                .collect(Collectors.toList());

        TextElementChangeFormatCommand command = new TextElementChangeFormatCommand(selectedText, newFormatList);
        command.execute();
        commandDeque.addCommand(command);

        TextElement firstElement = this.selectedText.get(0);
        // TODO do redraw better
        firstElement.redraw();
    }

    private void setItalic(Boolean isItalic) {
        this.textFormat = this.textFormat.withBold(isItalic);

        if (!isTextSelected()) {
            return;
        }

        List<TextFormat> newFormatList = this.selectedText.stream()//
                .map(v -> v.getFormat().withItalic(isItalic))//
                .collect(Collectors.toList());

        TextElementChangeFormatCommand command = new TextElementChangeFormatCommand(selectedText, newFormatList);
        command.execute();
        commandDeque.addCommand(command);

        TextElement firstElement = this.selectedText.get(0);
        // TODO do redraw better
        firstElement.redraw();
    }
}
