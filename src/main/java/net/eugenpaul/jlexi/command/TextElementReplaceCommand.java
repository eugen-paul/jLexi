package net.eugenpaul.jlexi.command;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementReplaceCommand implements TextCommand {

    private TextElement addedElement;
    @Getter
    private TextPosition cursorPosition;

    private TextElementRemoveSelectedCommand removeTextCommand;

    public TextElementReplaceCommand(TextElement addedElement, List<TextElement> selectedText) {
        this.addedElement = addedElement;
        this.removeTextCommand = new TextElementRemoveSelectedCommand(selectedText);
    }

    @Override
    public void execute() {
        this.removeTextCommand.execute();
        this.cursorPosition = this.removeTextCommand.getCursorPosition();
        this.cursorPosition.addBefore(addedElement);
    }

    @Override
    public void unexecute() {
        TextElementRemoveCommand undoCommand = new TextElementRemoveCommand(addedElement.getTextPosition());
        undoCommand.execute();
        this.removeTextCommand.unexecute();
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
