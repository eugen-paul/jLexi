package net.eugenpaul.jlexi.resourcesmanager;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontPixelsGenerator;

/**
 * Storage for chars as PixelArray generated from FontGenerator
 */
public class FontStorageImpl extends FontStorage {

    private final FontPixelsGenerator fontGenerator;

    private Map<String, Integer> fontToSize;
    private Map<Character, Drawable> charToArray;

    public FontStorageImpl(FontPixelsGenerator fontGenerator) {
        this.fontGenerator = fontGenerator;
        charToArray = new HashMap<>();
        fontToSize = new HashMap<>();

        init();
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    private void init() {
        int maxAscent = fontGenerator.getMaxAscent(DEFAULT_FONT_NAME, DEFAULT_STYLE, DEFAULT_FONT_SIZE);
        fontToSize.put(DEFAULT_FONT_NAME, maxAscent);

        // All printable Chars from ! to ~
        for (char c = 0x21; c < 0x7E; c++) {
            int[] pixels = fontGenerator.ofChar(c, DEFAULT_FONT_NAME, DEFAULT_STYLE, DEFAULT_FONT_SIZE);
            Drawable dr = new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
            charToArray.put(c, dr);
        }

        // Space
        int[] pixels = fontGenerator.ofChar(' ', DEFAULT_FONT_NAME, DEFAULT_STYLE, DEFAULT_FONT_SIZE);
        Drawable dr = new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
        charToArray.put(' ', dr);
    }

    @Override
    public Drawable ofChar(char c, String fontName, int style, int size) {
        return charToArray.getOrDefault(c, DEFAULT_DRAWABLE);
    }

}
