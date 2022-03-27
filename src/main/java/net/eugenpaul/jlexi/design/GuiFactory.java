package net.eugenpaul.jlexi.design;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public interface GuiFactory {
    Button createButton(Glyph parent, TextFormat format, ResourceManager storage);
}
