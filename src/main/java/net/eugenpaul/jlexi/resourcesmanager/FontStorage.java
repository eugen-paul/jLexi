package net.eugenpaul.jlexi.resourcesmanager;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;

/**
 * Abstract Storage for chars as PixelArray
 */
public abstract class FontStorage implements Resource {

    public static final int[] EMPTY_CHAR = new int[0];
    public static final String DEFAULT_FONT_NAME = "Monospaced";
    public static final int DEFAULT_FONT_SIZE = 12;

    protected static final DrawableV2 DEFAULT_DRAWABLE_2 = DrawableV2PixelsImpl.EMPTY;

    /**
     * Return Drawablr of char
     * 
     * @param c      - character
     * @param format - Format of the character
     * @return Drawable of the char
     */
    public abstract DrawableV2 ofChar2(Character c, TextFormat format);

    /**
     * Return max Pixel Ascent of font of given size
     * 
     * @param fontName - name of font
     * @param size     - size of font
     * @return max Pixel Ascent
     */
    public abstract int getMaxAscent(String fontName, int size);
}
