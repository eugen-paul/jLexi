package net.eugenpaul.jlexi.resourcesmanager;

import java.awt.Font;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Size;

/**
 * Abstract Storage for chars as PixelArray
 */
public abstract class FontStorage {

    public static final int[] EMPTY_CHAR = new int[0];
    public static final String DEFAULT_FONT_NAME = "Monospaced";
    public static final int DEFAULT_STYLE = Font.PLAIN;
    public static final int DEFAULT_FONT_SIZE = 12;

    protected static final Drawable DEFAULT_DRAWABLE = new DrawableImpl(EMPTY_CHAR, Size.ZERO_SIZE);

    /**
     * Return Pixel-Array of char
     * 
     * @param c        - character
     * @param fontName - font of character
     * @param style    - style of character (for example, Font.ITALIC or Font.BOLD|Font.ITALIC)
     * @param size     - size of character
     * @return Pixel-Array of the char
     */
    public abstract Drawable ofChar(char c, String fontName, int style, int size);
}
