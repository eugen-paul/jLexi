package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;

public class DarkFactory implements GuiFactory {

    protected static final Color BACKGROUND_COLOR = Color.fromHexARGB("0xFFBFBFBF");
    protected static final Color BACKGROUND_FOCUS_COLOR = Color.fromHexARGB("0xFFE2E2E2");
    protected static final Color BACKGROUND_PUSH_COLOR = Color.fromHexARGB("0xFFE2E2E2");
    protected static final Color BACKGROUND_CHECK_COLOR = Color.fromHexARGB("0xFFB2B2B3");

    private static final TextFormat format = TextFormat.DEFAULT.withBackgroundColor(BACKGROUND_COLOR);

    @Override
    public TextButton createTextButton(Glyph parent, String text, ResourceManager storage) {
        return new DarkTextButton(parent, text, format, storage);
    }

    @Override
    public Label createLabel(Glyph parent, String text, ResourceManager storage) {
        return new Label(parent, text, format, storage);
    }

}
