package net.eugenpaul.jlexi.resourcesmanager.font;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;

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
     * Get Drawable-Representation of char
     * 
     * @param c      - character
     * @param format - format of character
     * @return Drawable
     */
    public Drawable ofChar2(Character c, TextFormat format);

    /**
     * Get the maximum ascent (Ascent + Descent)
     * 
     * @param fontName
     * @param fontSize
     * @return max size of character
     */
    public int getMaxAscent(String fontName, int fontSize);

    /**
     * Get the ascent (distance from top to baseline)
     * 
     * @param fontName
     * @param fontSize
     * @return max size of character
     */
    public int getAscent(String fontName, int fontSize);

    /**
     * Get the descent (distance from baseline to bottom)
     * 
     * @param fontName
     * @param fontSize
     * @return max size of character
     */
    public int getDescent(String fontName, int fontSize);

    public int getStyle(TextFormat format);
}
