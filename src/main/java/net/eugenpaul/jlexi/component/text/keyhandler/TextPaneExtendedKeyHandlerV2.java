package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.command.TextCommandV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneExtendedKeyHandlerV2 extends AbstractKeyHandlerV2 {

    public TextPaneExtendedKeyHandlerV2(KeyHandlerableV2 component, ResourceManager storage,
            CommandsDeque<TextPositionV2, TextCommandV2> commandDeque) {
        super(component, storage, commandDeque);
    }

}
