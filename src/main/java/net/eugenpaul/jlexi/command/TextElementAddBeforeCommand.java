package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementAddBeforeCommand implements TextCommand {

    private TextElement addedElement;
    @Getter
    private TextPosition cursorPosition;

    public TextElementAddBeforeCommand(TextElement addedElement, TextPosition cursorPosition) {
        this.addedElement = addedElement;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void execute() {
        cursorPosition.addBefore(addedElement);
    }

    @Override
    public void unexecute() {
        TextElementRemoveCommant undoCommand = new TextElementRemoveCommant(addedElement.getTextPosition());
        undoCommand.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
