package net.eugenpaul.jlexi.command;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementRemoveSelectedCommand implements TextCommand {

    private TextPosition cursorPosition;
    private LinkedList<TextElementRemoveCommand> removedCommands;

    public TextElementRemoveSelectedCommand(List<TextElement> selectedText) {
        this.cursorPosition = null;
        this.removedCommands = selectedText.stream()//
                .map(v -> new TextElementRemoveCommand(v.getTextPosition()))//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void execute() {
        for (var command : this.removedCommands) {
            command.execute();
            this.cursorPosition = command.getData();
        }
    }

    @Override
    public void unexecute() {
        var invertIterator = this.removedCommands.descendingIterator();
        while (invertIterator.hasNext()) {
            var command = invertIterator.next();
            command.unexecute();
            this.cursorPosition = command.getData();
        }
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.removedCommands.isEmpty();
    }

    @Override
    public TextPosition getData() {
        return this.cursorPosition;
    }
}
