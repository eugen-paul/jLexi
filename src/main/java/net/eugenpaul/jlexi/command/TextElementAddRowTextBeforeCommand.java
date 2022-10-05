package net.eugenpaul.jlexi.command;

import java.util.LinkedList;

import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextElementAddRowTextBeforeCommand implements TextCommand {

    private LinkedList<TextElementAddBeforeCommand> addcommands;
    private TextPosition cursorPosition;

    public TextElementAddRowTextBeforeCommand(ResourceManager storage, String text, TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.addcommands = new LinkedList<>();
        for (var c : text.toCharArray()) {
            var element = TextElementFactory.fromChar(//
                    storage, //
                    c, //
                    cursorPosition.getTextElement().getFormat(), //
                    cursorPosition.getTextElement().getFormatEffect());

            this.addcommands.add(new TextElementAddBeforeCommand(element, cursorPosition));
        }
    }

    @Override
    public void execute() {
        addcommands.forEach(TextElementAddBeforeCommand::execute);
    }

    @Override
    public void unexecute() {
        var reverseIterator = addcommands.descendingIterator();
        while (reverseIterator.hasNext()) {
            reverseIterator.next().unexecute();
        }
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return addcommands.isEmpty();
    }

    @Override
    public TextPosition getData() {
        return this.cursorPosition;
    }

}
