package net.eugenpaul.jlexi.design.dark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.Button;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class DarkButton extends Button {

    private static final Logger LOGGER = LoggerFactory.getLogger(DarkButton.class);

    public DarkButton(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(parent, text, format, storage);
    }

    @Override
    public void press() {
        LOGGER.trace("DarkButton pressed! {}", getLabel());
    }

}
