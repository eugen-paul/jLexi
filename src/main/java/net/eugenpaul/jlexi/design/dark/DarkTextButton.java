package net.eugenpaul.jlexi.design.dark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilderComponent;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;

public class DarkTextButton extends TextButton {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(DarkTextButton.class);

    @Builder
    private DarkTextButton(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(//
                parent, //
                Border.builder()//
                        .parent(parent)//
                        .borderColor(DarkFactory.BORDER_COLOR)//
                        .backgroundColor(DarkFactory.BORDER_BACKGROUND_COLOR)//
                        .borderSize(DarkFactory.BORDER_SIZE)//
                        .getBuilderComponent(), //
                text, //
                format, //
                storage //
        );
    }

    @Builder
    private DarkTextButton(Glyph parent, BorderBuilderComponent borderBuilder, String text, TextFormat format,
            ResourceManager storage) {
        super(//
                parent, //
                borderBuilder, //
                text, //
                format, //
                storage //
        );
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
