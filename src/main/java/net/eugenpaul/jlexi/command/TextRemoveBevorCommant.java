package net.eugenpaul.jlexi.command;

import java.util.LinkedList;

import lombok.Getter;
import lombok.var;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextRemoveBevorCommant implements TextCommand {

    @Getter
    private TextElement cursorPosition;
    private TextElement removedText;

    public TextRemoveBevorCommant(TextElement cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.removedText = null;
    }

    @Override
    public void execute() {
        if (cursorPosition == null) {
            return;
        }

        var parentStructure = cursorPosition.getStructureParent();
        if (null == parentStructure) {
            return;
        }

        var removed = new LinkedList<TextElement>();
        boolean isRemoved = parentStructure.removeElementBefore(cursorPosition, removed);

        if (isRemoved) {
            cursorPosition.getStructureParent().notifyChange();
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
