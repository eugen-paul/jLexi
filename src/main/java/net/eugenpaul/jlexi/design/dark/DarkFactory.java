package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.Button;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class DarkFactory implements GuiFactory {

    @Override
    public Button createButton(Glyph parent, TextFormat format, ResourceManager storage) {
        return new DarkButton(parent, format, storage);
    }

}
