package net.eugenpaul.jlexi.design.dark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;

public class DarkTextButton extends TextButton {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(DarkTextButton.class);

    public DarkTextButton(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(parent, text, format, storage);
    }

    @Override
    protected Color getBgColorNormal() {
        return DarkFactory.BUTTON_BACKGROUND_COLOR;
    }

    @Override
    protected Color getBgColorFocus() {
        return DarkFactory.BUTTON_BACKGROUND_FOCUS_COLOR;
    }

    @Override
    protected Color getBgColorPush() {
        return DarkFactory.BUTTON_BACKGROUND_PUSH_COLOR;
    }

    @Override
    protected Color getBgColorCheck() {
        return DarkFactory.BUTTON_BACKGROUND_CHECK_COLOR;
    }

}
