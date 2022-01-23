package net.eugenpaul.jlexi.resourcesmanager.fontgenerator;

/**
 * Interface for Pixel-Array-Generator for chars.
 */
public interface FontPixelsGenerator {

    /**
     * Get Pixel-Array-Representation of char
     * 
     * @param c        - character
     * @param fontName - font of character
     * @param style    - style of character (for example, Font.ITALIC or Font.BOLD|Font.ITALIC)
     * @param size     - size of character
     * @return Pixel-Array
     */
    public int[] ofChar(Character c, String fontName, int style, int size);

    /**
     * Get max Size of char
     * 
     * @param fontName - font of character
     * @param style    - style of character (for example, Font.ITALIC or Font.BOLD|Font.ITALIC)
     * @param size     - size of character
     * @return max size of character
     */
    public int getMaxAscent(String fontName, int style, int size);
}
