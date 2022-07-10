package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneExtendedKeyHandler extends AbstractKeyHandler {

    public TextPaneExtendedKeyHandler(KeyHandlerable component, ResourceManager storage,
            TextCommandsDeque commandDeque) {
        super(component, storage, commandDeque);
    }

}
