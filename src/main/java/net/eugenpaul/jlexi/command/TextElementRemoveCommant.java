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

    public TextElementRemoveCommant(TextPosition cursorPosition) {
        this.positionBeforeRemove = cursorPosition;
        this.cursorPosition = cursorPosition;
        this.positionAfterRemove = null;
        this.removedElement = null;
    }

    @Override
    public void execute() {
        TextRemoveResponse removedData = positionBeforeRemove.removeElement();

        if (removedData != TextRemoveResponse.EMPTY) {
            positionAfterRemove = removedData.getNewCursorPosition();
            cursorPosition = positionAfterRemove;
            removedElement = removedData.getRemovedElement();
        }
    }

    @Override
    public void unexecute() {
        if (!isEmpty()) {
            TextElementAddBeforeCommand command = new TextElementAddBeforeCommand(removedElement, positionAfterRemove);
            command.execute();
            cursorPosition = positionBeforeRemove;
        }
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
