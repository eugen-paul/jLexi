package net.eugenpaul.jlexi.command;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementReplaceCommand implements TextCommand {

    private TextPosition cursorPosition;

    private TextElementRemoveSelectedCommand removeCommand;
    private TextElementAddBeforeCommand addCommand;

    public TextElementReplaceCommand(TextElement addedElement, List<TextElement> selectedText) {
        this.addCommand = new TextElementAddBeforeCommand(addedElement, selectedText.get(0).getTextPosition());
        this.removeCommand = new TextElementRemoveSelectedCommand(selectedText);
    }

    @Override
    public void execute() {
        this.addCommand.execute();
        this.removeCommand.execute();
        this.cursorPosition = this.removeCommand.getData();
    }

    @Override
    public void unexecute() {
        this.removeCommand.unexecute();
        this.addCommand.unexecute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public TextPosition getData() {
        return this.cursorPosition;
    }

}
