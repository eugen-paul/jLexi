package net.eugenpaul.jlexi.resourcesmanager;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

/**
 * Format-Storage for text elements
 */
public interface FormatStorage extends Resource {

    public TextFormat add(TextFormat format);

    public TextFormat setFontSize(TextFormat format, int size);

    public TextFormat setFontName(TextFormat format, String fontName);

    public TextFormat setBold(TextFormat format, boolean bold);

    public TextFormat setItalic(TextFormat format, boolean italic);

    public List<PixelsFormatter> formatter(TextFormat format);
}
