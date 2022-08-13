package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextElementRemoveCommant implements TextCommand {

    private TextElement removedElement;
    private TextPosition positionBeforeRemove;
    private TextPosition positionAfterRemove;
    @Getter
    private TextPosition cursorPosition;

    private TextRemoveResponse removedData;

    public TextElementRemoveCommant(TextPosition cursorPosition) {
        this.positionBeforeRemove = cursorPosition;
        this.cursorPosition = cursorPosition;
        this.positionAfterRemove = null;
        this.removedElement = null;
        this.removedData = TextRemoveResponse.EMPTY;
    }

    @Override
    public void execute() {
        this.removedData = positionBeforeRemove.removeElement();

        if (this.removedData != TextRemoveResponse.EMPTY) {
            positionAfterRemove = this.removedData.getNewCursorPosition();
            cursorPosition = positionAfterRemove;
            removedElement = this.removedData.getRemovedElement();
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
            command = new TextElementAddBeforeCommand(this.removedElement, this.positionAfterRemove);
        }
        command.execute();
        this.cursorPosition = positionBeforeRemove;
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return removedElement == null;
    }
}
