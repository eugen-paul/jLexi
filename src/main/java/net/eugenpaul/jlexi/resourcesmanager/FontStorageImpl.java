package net.eugenpaul.jlexi.resourcesmanager;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
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

    private Map<Character, // Character
            Map<Integer, // Style
                    Map<String, // FontName
                            Map<Integer, // Size
                                    Drawable>>>> storage;

    public FontStorageImpl(FontPixelsGenerator fontGenerator) {
        this.fontGenerator = fontGenerator;
        fontToSize = new HashMap<>();
        storage = new HashMap<>();
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    public Drawable ofChar(Character c, TextFormat format) {
        return ofChar(c, format.getFontName(), format.getStyle(), format.getFontsize());
    }

    @Override
    public Drawable ofChar(Character c, String fontName, int style, int size) {
        if (!CharacterHelper.isPrintable(c)) {
            return DEFAULT_DRAWABLE;
        }

        Integer maxAscent = fontToSize.computeIfAbsent(fontName, k -> getMaxAscent(k, size));

        var response = storage.computeIfAbsent(c, key -> {
            int[] pixels = fontGenerator.ofChar(c, fontName, style, size);
            Drawable drawable = new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));

            Map<Integer, // Size
                    Drawable> sizeMap = new HashMap<>();
            sizeMap.put(size, drawable);

            Map<String, // FontName
                    Map<Integer, // Size
                            Drawable>> fontMap = new HashMap<>();
            fontMap.put(fontName, sizeMap);

            Map<Integer, // Style
                    Map<String, // FontName
                            Map<Integer, // Size
                                    Drawable>>> styleMap = new HashMap<>();
            styleMap.put(style, fontMap);

            return styleMap;
        }).computeIfAbsent(style, key -> {
            int[] pixels = fontGenerator.ofChar(c, fontName, style, size);
            Drawable drawable = new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));

            Map<Integer, // Size
                    Drawable> sizeMap = new HashMap<>();
            sizeMap.put(size, drawable);

            Map<String, // FontName
                    Map<Integer, // Size
                            Drawable>> fontMap = new HashMap<>();
            fontMap.put(fontName, sizeMap);
            return fontMap;
        }).computeIfAbsent(fontName, key -> {
            int[] pixels = fontGenerator.ofChar(c, fontName, style, size);
            Drawable drawable = new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));

            Map<Integer, // Size
                    Drawable> sizeMap = new HashMap<>();
            sizeMap.put(size, drawable);
            return sizeMap;
        }).computeIfAbsent(size, key -> {
            int[] pixels = fontGenerator.ofChar(c, fontName, style, size);
            return new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
        });

        return response.copy();
    }

    @Override
    public int getMaxAscent(String fontName, int size) {
        return fontToSize.getOrDefault(fontName, size);
    }

}
