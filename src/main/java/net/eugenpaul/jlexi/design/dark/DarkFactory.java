package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.menubar.MenuBar;
import net.eugenpaul.jlexi.component.menubar.MenuBarColored;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class DarkFactory implements GuiFactory {

    protected static final Color MENU_BACKGROUND_COLOR = Color.fromHexArgb("0xFF505050");

    protected static final Color BUTTON_BACKGROUND_COLOR = Color.fromHexArgb("0xFFBFBFBF");
    protected static final Color BUTTON_BACKGROUND_FOCUS_COLOR = Color.fromHexArgb("0xFFE2E2E2");
    protected static final Color BUTTON_BACKGROUND_PUSH_COLOR = Color.fromHexArgb("0xFFE2E2E2");
    protected static final Color BUTTON_BACKGROUND_CHECK_COLOR = Color.fromHexArgb("0xFFB2B2B3");

    protected static final Color BORDER_COLOR = Color.fromHexArgb("0xFF000000");
    protected static final Color BORDER_BACKGROUND_COLOR = Color.fromHexArgb("0xFFBFBFBF");

    private static final TextFormat format = TextFormat.DEFAULT.withBackgroundColor(BUTTON_BACKGROUND_COLOR);

    @Override
    public TextButton createTextButton(Glyph parent, String text, ResourceManager storage) {
        return new DarkTextButton(parent, this, text, format, storage);
    }

    @Override
    public Label createLabel(Glyph parent, String text, ResourceManager storage) {
        return new Label(parent, text, format, storage);
    }

    @Override
    public MenuBar createMenuBar(Glyph parent, GuiGlyph component, Size size) {
        return new MenuBarColored(parent, component, size, MENU_BACKGROUND_COLOR);
    }

    @Override
    public Border createBorder(Glyph parent, GuiGlyph component) {
        return new Border(parent, component, BORDER_COLOR, BORDER_BACKGROUND_COLOR);
    }

}
