package net.eugenpaul.jlexi.command;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementAddFormatTextBeforeCommand implements TextCommand {

    private LinkedList<TextElementAddBeforeCommand> addcommands;
    @Getter
    private TextPosition cursorPosition;

    public TextElementAddFormatTextBeforeCommand(List<TextElement> text, TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.addcommands = new LinkedList<>();
        for (var element : text) {
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

}
