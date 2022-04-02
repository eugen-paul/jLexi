package net.eugenpaul.jlexi.design;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public interface GuiFactory {
    Button createButton(Glyph parent, String text, ResourceManager storage);

    Label createLabel(Glyph parent, String text, ResourceManager storage);
}
