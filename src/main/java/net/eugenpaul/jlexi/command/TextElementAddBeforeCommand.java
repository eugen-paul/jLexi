package net.eugenpaul.jlexi.command;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextElementAddBeforeCommand implements TextCommand {

    private TextElement addedElement;
    @Getter
    private TextPosition cursorPosition;

    private List<List<TextStructure>> removedStructures;
    private List<TextStructure> newStructures;

    public TextElementAddBeforeCommand(TextElement addedElement, TextPosition cursorPosition) {
        this.addedElement = addedElement;
        this.cursorPosition = cursorPosition;
        this.removedStructures = null;
        this.newStructures = null;
    }

    public TextElementAddBeforeCommand(TextRemoveResponse removedData) {
        this.addedElement = removedData.getRemovedElement();
        this.cursorPosition = removedData.getNewCursorPosition();
        this.removedStructures = removedData.getRemovedStructures();
        this.newStructures = removedData.getNewStructures();
    }

    @Override
    public void execute() {
        if (newStructures == null || removedStructures == null) {
            cursorPosition.addBefore(addedElement);
        } else {
            cursorPosition.splitStructures(newStructures, removedStructures);
        }
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
