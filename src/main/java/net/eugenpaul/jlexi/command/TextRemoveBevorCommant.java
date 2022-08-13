package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextRemoveBevorCommant implements TextCommand {

    @Getter
    private TextPosition cursorPosition;
    private TextElement removedElement;

    TextRemoveResponse removedData;

    public TextRemoveBevorCommant(TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.removedElement = null;
        this.removedData = TextRemoveResponse.EMPTY;
    }

    @Override
    public void execute() {
        var previousPosition = this.cursorPosition.getPreviousPosition();
        if (previousPosition != null) {
            removedData = previousPosition.removeElement();

            if (removedData != TextRemoveResponse.EMPTY) {
                this.cursorPosition = removedData.getNewCursorPosition();
                this.removedElement = removedData.getRemovedElement();
            }
        }
    }

    @Override
    public void unexecute() {
        if (isEmpty()) {
            return;
        }

        TextElementAddBeforeCommand command;
        if (this.removedData.isTextReplaced()) {
            command = new TextElementAddBeforeCommand(this.removedData);
        } else {
            command = new TextElementAddBeforeCommand(this.removedElement, this.cursorPosition);
        }
        command.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.removedElement == null;
    }

}
