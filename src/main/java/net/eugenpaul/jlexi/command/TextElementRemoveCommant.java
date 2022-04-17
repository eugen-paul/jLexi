package net.eugenpaul.jlexi.command;

import java.util.LinkedList;

import lombok.Getter;
import lombok.var;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

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
        TextPosition newCursorPosition = null;

        var removed = new LinkedList<TextElement>();
        newCursorPosition = positionBeforeRemove.removeElement(removed);

        if (newCursorPosition != null) {
            positionBeforeRemove.notifyChange();
            positionAfterRemove = newCursorPosition;
            cursorPosition = positionAfterRemove;
            removedElement = removed.getFirst();
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
