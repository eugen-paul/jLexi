package net.eugenpaul.jlexi.component.text.format.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;

public class Italic<T extends TextPaneElement> extends TextFormatContainer<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Italic.class);

    public Italic(Glyph parent, TextFormatContainer<T> formatParent) {
        super(parent, formatParent, null);
        getFormat().setItalic(true);
    }

}
