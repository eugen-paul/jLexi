package net.eugenpaul.jlexi.design;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public interface GuiFactory {

    TextButton createTextButton(Glyph parent, String text, ResourceManager storage);

    Label createLabel(Glyph parent, String text, ResourceManager storage);
}
