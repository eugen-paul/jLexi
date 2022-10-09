package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.Deque;
import java.util.LinkedList;

import net.eugenpaul.jlexi.command.Command;

public class CommandsDeque<D, T extends Command<D>> {
    private final Deque<T> undoCommands;
    private final Deque<T> redoCommands;

    public CommandsDeque() {
        this.undoCommands = new LinkedList<>();
        this.redoCommands = new LinkedList<>();
    }

    public void addCommand(T command) {
        if (command.reversible()) {
            this.undoCommands.add(command);
        } else {
            this.undoCommands.clear();
        }

        this.redoCommands.clear();
    }

    public D undo() {
        var command = this.undoCommands.pollLast();
        if (null == command) {
            return null;
        }

        command.unexecute();
        this.redoCommands.add(command);

        return command.getData();
    }

    public D redo() {
        var command = this.redoCommands.pollLast();
        if (null == command) {
            return null;
        }

        command.execute();
        this.undoCommands.add(command);

        return command.getData();
    }
}
