package net.eugenpaul.jlexi.resourcesmanager.fontgenerator;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

/**
 * Interface for Pixel-Array-Generator for chars.
 */
public interface FontPixelsGenerator {

    /**
     * Get Pixel-Array-Representation of char
     * 
     * @param c      - character
     * @param format - format of character
     * @return Pixel-Array
     */
    public int[] ofChar(Character c, TextFormat format);

    /**
     * Get max Size of char
     * 
     * @param format - format of character
     * @return max size of character
     */
    public int getMaxAscent(TextFormat format);

    public int getStyle(TextFormat format);
}
