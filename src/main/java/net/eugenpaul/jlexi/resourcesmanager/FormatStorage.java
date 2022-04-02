package net.eugenpaul.jlexi.resourcesmanager;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;

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
     * Get immutable List of PixelsFormatter to given formatEffect
     * 
     * @param formatEffect
     * @return
     */
    public List<PixelsFormatter> formatter(TextFormatEffect formatEffect);

    public TextFormatEffect add(TextFormatEffect formatEffect);
}
