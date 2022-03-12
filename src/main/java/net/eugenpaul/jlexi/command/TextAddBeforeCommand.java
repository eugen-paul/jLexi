package net.eugenpaul.jlexi.command;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextAddBeforeCommand implements Command {

    private List<TextElement> text;
    private TextElement position;

    public TextAddBeforeCommand(List<TextElement> text, TextElement position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public void execute() {
        var parentStructure = position.getStructureParent();

        if (null == parentStructure) {
            return;
        }

        text.stream().forEach(element -> parentStructure.addBefore(position, element));
        parentStructure.notifyChange(true);
    }

    @Override
    public void unexecute() {
        TextRemoveCommant undoCommand = new TextRemoveCommant(text, position);
        undoCommand.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

}
