package net.eugenpaul.jlexi.component.text;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import net.eugenpaul.jlexi.appl.subscriber.GlobalSubscribeTypes;
import net.eugenpaul.jlexi.appl.subscriber.SchedulerSub;
import net.eugenpaul.jlexi.command.TextCommandV2;
import net.eugenpaul.jlexi.command.TextElementChangeFormatCommandV2;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentRoot;
import net.eugenpaul.jlexi.component.text.keyhandler.CommandsDeque;
import net.eugenpaul.jlexi.effect.CursorEffectV2;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.effect.SelectedEffectV2;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;

public class CursorV2 implements EventSubscriber {

    @Getter
    private final String name;
    private final CommandsDeque<TextPositionV2, TextCommandV2> commandDeque;

    private TextElementV2 textElement;

    @Getter
    private TextFormat textFormat;
    @Getter
    private TextFormatEffect textFormatEffect;

    private GlyphEffect cursorEffect;
    private EventManager eventManager;

    @Getter
    private List<TextElementV2> selectedText;
    private GlyphEffect selectedTextEffect;

    private TextPaneDocumentRoot docRoot;

    public CursorV2(TextElementV2 glyphElement, EventManager eventManager, String name,
            CommandsDeque<TextPositionV2, TextCommandV2> commandDeque, TextPaneDocumentRoot docRoot) {
        this.name = name;
        this.commandDeque = commandDeque;
        this.textElement = glyphElement;
        this.cursorEffect = null;
        this.eventManager = eventManager;
        this.eventManager.addSubscriber(this);
        this.docRoot = docRoot;

        this.selectedText = null;
        this.selectedTextEffect = null;

    }

    public TextPositionV2 getPosition() {
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

        this.selectedTextEffect = new SelectedEffectV2(this.selectedText, this.docRoot);
        this.eventManager.fireEvent(this, SchedulerSub.ADD_EVENT, this.selectedTextEffect);
    }

    private void removeSelectedEffect() {
        if (this.selectedTextEffect == null) {
            // nothing to remove
            return;
        }

        for (var element : this.selectedText) {
            element.removeEffect(this.selectedTextEffect);
        }

        this.eventManager.fireEvent(this, SchedulerSub.REMOVE_EVENT, this.selectedTextEffect);

        this.selectedText = null;
        this.selectedTextEffect = null;
    }

    public void setTextSelection(List<TextElementV2> selection) {
        removeSelectedEffect();
        this.selectedText = selection;
        addSelectedEffect();
    }

    public void moveCursorTo(TextPositionV2 cursorPosition) {
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

        this.cursorEffect = new CursorEffectV2(this.textElement, this.docRoot);
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

        var command = new TextElementChangeFormatCommandV2(selectedText, newFormatList);
        command.execute();
        commandDeque.addCommand(command);

        docRoot.redrawDocument();
    }

    private void setItalic(Boolean isItalic) {
        this.textFormat = this.textFormat.withBold(isItalic);

        if (!isTextSelected()) {
            return;
        }

        List<TextFormat> newFormatList = this.selectedText.stream()//
                .map(v -> v.getFormat().withItalic(isItalic))//
                .collect(Collectors.toList());

        var command = new TextElementChangeFormatCommandV2(selectedText, newFormatList);
        command.execute();
        commandDeque.addCommand(command);

        docRoot.redrawDocument();
    }
}
