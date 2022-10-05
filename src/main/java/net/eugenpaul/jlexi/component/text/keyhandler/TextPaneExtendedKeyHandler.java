package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneExtendedKeyHandler extends AbstractKeyHandler {

    public TextPaneExtendedKeyHandler(KeyHandlerable component, ResourceManager storage,
            CommandsDeque<TextPosition, TextCommand> commandDeque) {
        super(component, storage, commandDeque);
    }

}
