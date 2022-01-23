package net.eugenpaul.jlexi.resourcesmanager;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontPixelsGenerator;
import net.eugenpaul.jlexi.utils.CharacterHelper;

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

        int maxAscent = fontGenerator.getMaxAscent(DEFAULT_FONT_NAME, DEFAULT_STYLE, DEFAULT_FONT_SIZE);
        fontToSize.put(DEFAULT_FONT_NAME, maxAscent);
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    @Override
    public Drawable ofChar(Character c, String fontName, int style, int size) {
        if (!CharacterHelper.isPrintable(c)) {
            return DEFAULT_DRAWABLE;
        }

        int maxAscent = fontToSize.get(DEFAULT_FONT_NAME);

        return charToArray.computeIfAbsent(c, key -> {
            int[] pixels = fontGenerator.ofChar(c, DEFAULT_FONT_NAME, DEFAULT_STYLE, DEFAULT_FONT_SIZE);
            return new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
        });
    }

    @Override
    public int getMaxAscent(String fontName, int size) {
        return fontToSize.getOrDefault(DEFAULT_FONT_NAME, DEFAULT_MAX_ASCENT);
    }

}
