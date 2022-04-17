package net.eugenpaul.jlexi.command;

import java.util.LinkedList;

import lombok.Getter;
import lombok.var;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextRemoveBevorCommant implements TextCommand {

    @Getter
    private TextPosition cursorPosition;
    private TextElement removedText;

    public TextRemoveBevorCommant(TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.removedText = null;
    }

    @Override
    public void execute() {
        if (cursorPosition == null) {
            return;
        }

        var removed = new LinkedList<TextElement>();
        boolean isRemoved = cursorPosition.removeElementBefore(removed);

        if (isRemoved) {
            cursorPosition.notifyChange();
            removedText = removed.getFirst();
        }
    }

    @Override
    public void unexecute() {
        TextElementAddBeforeCommand command = new TextElementAddBeforeCommand(removedText, cursorPosition);
        command.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return removedText == null;
    }

}
