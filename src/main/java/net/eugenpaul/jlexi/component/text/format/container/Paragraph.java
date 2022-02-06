package net.eugenpaul.jlexi.component.text.format.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;

public class Paragraph<T extends TextPaneElement> extends TextFormatContainer<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Paragraph.class);

    public Paragraph(Glyph parent, TextFormatContainer<T> formatParent) {
        super(parent, formatParent, null);
    }

    @Override
    public boolean isEndOfLine() {
        return true;
    }

}
