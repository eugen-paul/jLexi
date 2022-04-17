package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextElementAddBeforeCommand implements TextCommand {

    private TextElement addedElement;
    @Getter
    private TextElement cursorPosition;

    public TextElementAddBeforeCommand(TextElement addedElement, TextElement cursorPosition) {
        this.addedElement = addedElement;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void execute() {
        cursorPosition.addBefore(addedElement);
        cursorPosition.notifyChange();
    }

    @Override
    public void unexecute() {
        TextElementRemoveCommant undoCommand = new TextElementRemoveCommant(addedElement);
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
