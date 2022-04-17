package net.eugenpaul.jlexi.command;

import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public interface TextCommand extends Command {
    public abstract TextPosition getCursorPosition();
}
