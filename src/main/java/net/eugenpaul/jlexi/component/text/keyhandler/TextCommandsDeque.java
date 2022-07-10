package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.Deque;
import java.util.LinkedList;

import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextCommandsDeque {
    private final Deque<TextCommand> undoCommands;
    private final Deque<TextCommand> redoCommands;

    public TextCommandsDeque() {
        this.undoCommands = new LinkedList<>();
        this.redoCommands = new LinkedList<>();
    }

    public void addCommand(TextCommand command) {
        if (command.reversible()) {
            this.undoCommands.add(command);
        } else {
            this.undoCommands.clear();
        }

        this.redoCommands.clear();
    }

    public TextPosition undo() {
        var command = this.undoCommands.pollLast();
        if (null == command) {
            return null;
        }

        command.unexecute();
        this.redoCommands.add(command);

        return command.getCursorPosition();
    }

    public TextPosition redo() {
        var command = this.redoCommands.pollLast();
        if (null == command) {
            return null;
        }

        command.execute();
        this.undoCommands.add(command);

        return command.getCursorPosition();
    }
}
