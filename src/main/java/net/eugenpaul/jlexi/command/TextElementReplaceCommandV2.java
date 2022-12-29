package net.eugenpaul.jlexi.command;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;

public class TextElementReplaceCommandV2 implements TextCommandV2 {

    private TextPositionV2 cursorPosition;

    private TextElementRemoveSelectedCommandV2 removeCommand;
    private TextElementAddBeforeCommandV2 addCommand;

    public TextElementReplaceCommandV2(TextElementV2 addedElement, List<TextElementV2> selectedText) {
        this.addCommand = new TextElementAddBeforeCommandV2(addedElement, selectedText.get(0).getTextPosition());
        this.removeCommand = new TextElementRemoveSelectedCommandV2(selectedText);
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
    public TextPositionV2 getData() {
        return this.cursorPosition;
    }

}
