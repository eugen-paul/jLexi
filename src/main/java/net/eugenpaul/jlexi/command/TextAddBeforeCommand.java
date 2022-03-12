package net.eugenpaul.jlexi.command;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextAddBeforeCommand implements TextCommand {

    private List<TextElement> text;
    @Getter
    private TextElement cursorPosition;

    public TextAddBeforeCommand(List<TextElement> text, TextElement cursorPosition) {
        this.text = text;
        this.cursorPosition = cursorPosition;
    }

    @Override
    public void execute() {
        var parentStructure = cursorPosition.getStructureParent();

        if (null == parentStructure) {
            return;
        }

        text.stream().forEach(element -> parentStructure.addBefore(cursorPosition, element));
        parentStructure.notifyChange(true);
    }

    @Override
    public void unexecute() {
        TextRemoveCommant undoCommand = new TextRemoveCommant(text, cursorPosition);
        undoCommand.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

}
