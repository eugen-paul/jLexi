package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.Button;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.design.Label;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;

public class DarkFactory implements GuiFactory {

    private static final Color BACKGROUND_COLOR = Color.fromHexARGB("0xFFBFBFBF");

    private static final TextFormat format = TextFormat.DEFAULT.withBackgroundColor(BACKGROUND_COLOR);

    @Override
    public Button createButton(Glyph parent, String text, ResourceManager storage) {
        return new DarkButton(parent, text, format, storage);
    }

    @Override
    public Label createLabel(Glyph parent, String text, ResourceManager storage) {
        return new Label(parent, text, format, storage);
    }

}
