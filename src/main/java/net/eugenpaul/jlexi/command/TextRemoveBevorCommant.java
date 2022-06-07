package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextRemoveBevorCommant implements TextCommand {

    @Getter
    private TextPosition cursorPosition;
    private TextElement removedElement;

    public TextRemoveBevorCommant(TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.removedElement = null;
    }

    @Override
    public void execute() {
        TextRemoveResponse removedData = cursorPosition.removeElementBefore();

        if (removedData != TextRemoveResponse.EMPTY) {
            cursorPosition = removedData.getNewCursorPosition();
            removedElement = removedData.getRemovedElement();
        }
    }

    @Override
    public void unexecute() {
        TextElementAddBeforeCommand command = new TextElementAddBeforeCommand(removedElement, cursorPosition);
        command.execute();
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
