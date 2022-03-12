package net.eugenpaul.jlexi.command;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface TextCommand extends Command {
    public abstract TextElement getCursorPosition();
}
