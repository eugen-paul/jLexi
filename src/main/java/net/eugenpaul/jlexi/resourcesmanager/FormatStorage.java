package net.eugenpaul.jlexi.resourcesmanager;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

/**
 * Format-Storage for text elements
 */
public interface FormatStorage extends Resource{

    public TextFormat of(FormatAttribute format);
}
