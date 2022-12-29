package net.eugenpaul.jlexi.command;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;

public class TextElementRemoveSelectedCommandV2 implements TextCommandV2 {

    private TextPositionV2 cursorPosition;
    private LinkedList<TextElementRemoveCommandV2> removedCommands;

    public TextElementRemoveSelectedCommandV2(List<TextElementV2> selectedText) {
        this.cursorPosition = null;
        this.removedCommands = selectedText.stream()//
                .map(v -> new TextElementRemoveCommandV2(v.getTextPosition()))//
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
    public TextPositionV2 getData() {
        return this.cursorPosition;
    }
}
