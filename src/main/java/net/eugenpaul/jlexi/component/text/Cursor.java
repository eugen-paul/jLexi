package net.eugenpaul.jlexi.component.text;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import net.eugenpaul.jlexi.appl.subscriber.GlobalSubscribeTypes;
import net.eugenpaul.jlexi.appl.subscriber.SchedulerSub;
import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextElementChangeFormatCommand;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.keyhandler.CommandsDeque;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.effect.SelectedEffect;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;

public class Cursor implements EventSubscriber {

    @Getter
    private final String name;
    private final CommandsDeque<TextPosition, TextCommand> commandDeque;

    private TextElement textElement;

    @Getter
    private TextFormat textFormat;
    @Getter
    private TextFormatEffect textFormatEffect;

    private GlyphEffect cursorEffect;
    private EventManager eventManager;

    @Getter
    private List<TextElement> selectedText;
    private GlyphEffect selectedTextEffect;

    public Cursor(TextElement glyphElement, EventManager eventManager, String name,
            CommandsDeque<TextPosition, TextCommand> commandDeque) {
        this.name = name;
        this.commandDeque = commandDeque;
        this.textElement = glyphElement;
        this.cursorEffect = null;
        this.eventManager = eventManager;
        this.eventManager.addSubscriber(this);

        this.selectedText = null;
        this.selectedTextEffect = null;

    }

    public TextPosition getPosition() {
        if (this.textElement != null) {
            return this.textElement.getTextPosition();
        }
        return null;
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

        this.selectedTextEffect = new SelectedEffect(this.selectedText);
        this.eventManager.fireEvent(this, SchedulerSub.ADD_EVENT, selectedTextEffect);
    }

    private void removeSelectedEffect() {
        if (this.selectedTextEffect == null) {
            // nothing to remove
            return;
        }

        for (var element : this.selectedText) {
            element.removeEffect(selectedTextEffect);
        }

        this.eventManager.fireEvent(this, SchedulerSub.REMOVE_EVENT, selectedTextEffect);

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
            this.eventManager.fireEvent(this, SchedulerSub.REMOVE_EVENT, cursorEffect);
        }

        if (null == cursorPosition) {
            return;
        }

        this.textElement = cursorPosition.getTextElement();
        this.textFormat = this.textElement.getFormat();
        this.textFormatEffect = this.textElement.getFormatEffect();

        this.cursorEffect = new CursorEffect(this.textElement);
        this.eventManager.fireEvent(this, SchedulerSub.ADD_EVENT, cursorEffect);
        this.eventManager.fireEvent(this, GlobalSubscribeTypes.TEXT_CURSOR_MOVE, this.textElement);
    }

    @Override
    public void update(Object source, Object type, Object data) {
        // TODO check source
        if (type == GlobalSubscribeTypes.TEXT_FORMAT_ELEMENT && data instanceof TextFormat) {
            setBold(((TextFormat) data).getBold());
            setItalic(((TextFormat) data).getItalic());
        }
    }

    public void switchBold() {
        if (this.textElement == null) {
            return;
        }
        setBold(!this.textElement.getFormat().getBold().booleanValue());
    }

    public void switchItalic() {
        if (this.textElement == null) {
            return;
        }
        setItalic(!this.textElement.getFormat().getItalic().booleanValue());
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
