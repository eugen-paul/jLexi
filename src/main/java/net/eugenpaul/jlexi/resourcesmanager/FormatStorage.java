package net.eugenpaul.jlexi.resourcesmanager;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformatter.FormatterTypeParameter;

/**
 * Format-Storage for text elements
 */
public interface FormatStorage extends Resource {

    public TextFormat add(TextFormat format);

    public TextFormat setFontSize(TextFormat format, int size);

    public TextFormat setFontName(TextFormat format, String fontName);

    public TextFormat setBold(TextFormat format, boolean bold);

    public TextFormat setItalic(TextFormat format, boolean italic);

    public List<PixelsFormatter> formatter(TextFormatEffect format);

    public TextFormatEffect add(TextFormatEffect format);

    public <T> TextFormatEffect setFormatEffect(TextFormatEffect format, FormatterTypeParameter<T> parameter,
            T value);
}
