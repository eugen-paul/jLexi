package net.eugenpaul.jlexi.design.dark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilder;
import net.eugenpaul.jlexi.component.button.ImageButton;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.utils.Color;

public class DarkImageButton extends ImageButton {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(DarkImageButton.class);

    @Builder
    public DarkImageButton(Glyph parent, ImageGlyph image, BorderBuilder borderBuilder) {
        super(parent, image, borderBuilder);
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
