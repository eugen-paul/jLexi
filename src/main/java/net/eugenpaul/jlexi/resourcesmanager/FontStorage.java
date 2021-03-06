package net.eugenpaul.jlexi.resourcesmanager;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;

/**
 * Abstract Storage for chars as PixelArray
 */
public abstract class FontStorage implements Resource {

    public static final int[] EMPTY_CHAR = new int[0];
    public static final String DEFAULT_FONT_NAME = "Monospaced";
    public static final int DEFAULT_FONT_SIZE = 12;

    protected static final Drawable DEFAULT_DRAWABLE_2 = DrawablePixelsImpl.EMPTY;

    /**
     * Return Drawablr of char
     * 
     * @param c      - character
     * @param format - Format of the character
     * @return Drawable of the char
     */
    public abstract Drawable ofChar2(Character c, TextFormat format);

    /**
     * Return max Pixel Ascent of font of given size
     * 
     * @param fontName - name of font
     * @param size     - size of font
     * @return max Pixel Ascent
     */
    public abstract int getMaxAscent(String fontName, int size);

    /**
     * Return pixel ascent of font of given size (distance from top to baseline)
     * 
     * @param fontName - name of font
     * @param size     - size of font
     * @return ascent
     */
    public abstract int getAscent(String fontName, int size);

    /**
     * Return pixel descent of font of given size (distance from baseline to bottom)
     * 
     * @param fontName - name of font
     * @param size     - size of font
     * @return descent
     */
    public abstract int getDescent(String fontName, int size);
}
