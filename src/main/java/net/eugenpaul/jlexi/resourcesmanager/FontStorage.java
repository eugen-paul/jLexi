package net.eugenpaul.jlexi.resourcesmanager;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;

/**
 * Abstract Storage for chars as PixelArray
 */
public abstract class FontStorage implements Resource {

    public static final int[] EMPTY_CHAR = new int[0];
    public static final String DEFAULT_FONT_NAME = "Monospaced";
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final int DEFAULT_MAX_ASCENT = 7;

    protected static final Drawable DEFAULT_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    /**
     * Return Pixel-Array of char
     * 
     * @param c      - character
     * @param format - Format of the character
     * @return Drawable of the char
     */
    public abstract Drawable ofChar(Character c, TextFormat format);

    /**
     * Return max Pixel Ascent of font of given size
     * 
     * @param fontName - name of font
     * @param size     - size of font
     * @return max Pixel Ascent
     */
    public abstract int getMaxAscent(String fontName, int size);
}
