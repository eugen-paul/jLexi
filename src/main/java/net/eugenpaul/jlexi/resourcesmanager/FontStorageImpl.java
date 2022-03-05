package net.eugenpaul.jlexi.resourcesmanager;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontPixelsGenerator;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

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
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    @Override
    public Drawable ofChar(Character c, String fontName, int style, int size) {
        if (!CharacterHelper.isPrintable(c)) {
            return DEFAULT_DRAWABLE;
        }

        Integer maxAscent = fontToSize.computeIfAbsent(fontName, k -> getMaxAscent(k, size));

        return charToArray.computeIfAbsent(c, key -> {
            int[] pixels = fontGenerator.ofChar(c, fontName, style, size);
            return new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
        }).copy();
    }

    @Override
    public int getMaxAscent(String fontName, int size) {
        return fontToSize.getOrDefault(fontName, size);
    }

}
