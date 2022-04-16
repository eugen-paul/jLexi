package net.eugenpaul.jlexi.command;

import lombok.Getter;
import lombok.var;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextElementRemoveCommant implements TextCommand {

    private TextElement elementToRemove;
    private TextElement removedElement;
    @Getter
    private TextElement cursorPosition;

    public TextElementRemoveCommant(TextElement elementToRemove, TextElement cursorPosition) {
        this.elementToRemove = elementToRemove;
        this.cursorPosition = cursorPosition;
        this.removedElement = null;
    }

    public TextElementRemoveCommant(TextElement elementToRemove) {
        this(elementToRemove, null);
    }

    @Override
    public void execute() {
        TextElement newCursorPosition = null;
        var parentStructure = elementToRemove.getStructureParent();
        if (null == parentStructure) {
            return;
        }

        newCursorPosition = parentStructure.removeElement(elementToRemove);
        
        if (newCursorPosition != null) {
            parentStructure.notifyChange();
            cursorPosition = newCursorPosition;
            removedElement = elementToRemove;
        }
    }

    @Override
    public void unexecute() {
        if (!isEmpty()) {
            TextElementAddBeforeCommand command = new TextElementAddBeforeCommand(removedElement, cursorPosition);
            command.execute();
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
