package net.eugenpaul.jlexi.command;

import net.eugenpaul.jlexi.component.interfaces.CursorPosition;

public interface TextCommand extends Command {
    public abstract CursorPosition getCursorPosition();
}
