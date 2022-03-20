package net.eugenpaul.jlexi.resourcesmanager;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;
import net.eugenpaul.jlexi.resourcesmanager.textformat.textformatter.FormatterTypeParameter;

/**
 * Text-Format-Storage
 */
public interface FormatStorage extends Resource {

    /**
     * The format is added to storage if the format is new. If the format is already in storage, then the saved format
     * is returned.
     * 
     * @param format
     * @return format if the format is new or previous format
     */
    public TextFormat add(TextFormat format);

    /**
     * Set size by format and add it to storage if the changed format is new. If the format is already in storage, then
     * the saved format is returned.
     * 
     * @param format
     * @param size
     * @return format if the format is new or previous format
     */
    public TextFormat setFontSize(TextFormat format, int size);

    /**
     * Set fontName by format and add it to storage if the changed format is new. If the format is already in storage,
     * then the saved format is returned.
     * 
     * @param format
     * @param fontName
     * @return format if the format is new or previous format
     */
    public TextFormat setFontName(TextFormat format, String fontName);

    /**
     * Set bold by format and add it to storage if the changed format is new. If the format is already in storage, then
     * the saved format is returned.
     * 
     * @param format
     * @param bold
     * @return format if the format is new or previous format
     */
    public TextFormat setBold(TextFormat format, boolean bold);

    /**
     * Set italic by format and add it to storage if the changed format is new. If the format is already in storage,
     * then the saved format is returned.
     * 
     * @param format
     * @param italic
     * @return format if the format is new or previous format
     */
    public TextFormat setItalic(TextFormat format, boolean italic);

    /**
     * Get immutable List of PixelsFormatter to given formatEffect
     * 
     * @param formatEffect
     * @return
     */
    public List<PixelsFormatter> formatter(TextFormatEffect formatEffect);

    public TextFormatEffect add(TextFormatEffect formatEffect);

    public <T> TextFormatEffect setFormatEffect(TextFormatEffect formatEffect, FormatterTypeParameter<T> parameter,
            T value);
}
