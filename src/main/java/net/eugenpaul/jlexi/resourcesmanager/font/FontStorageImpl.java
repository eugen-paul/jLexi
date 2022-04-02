package net.eugenpaul.jlexi.resourcesmanager.font;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

/**
 * Storage for chars as PixelArray generated from FontGenerator
 */
public class FontStorageImpl extends FontStorage {

    private final FontPixelsGenerator fontGenerator;

    private Map<String, // FontName
            Map<Integer, // FontSize
                    Integer>> maxAscentStorage;

    private Map<Character, // Character
            Map<Integer, // Style
                    Map<String, // FontName
                            Map<Integer, // Size
                                    Map<Color, // FontColor
                                            Map<Color, // BackgroundColor
                                                    Drawable>>>>>> pixelStorage;

    public FontStorageImpl(FontPixelsGenerator fontGenerator) {
        this.fontGenerator = fontGenerator;
        this.maxAscentStorage = new HashMap<>();
        this.pixelStorage = new HashMap<>();
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    public Drawable ofChar(Character c, TextFormat format) {
        if (!CharacterHelper.isPrintable(c)) {
            return DEFAULT_DRAWABLE;
        }

        Integer style = fontGenerator.getStyle(format);

        Integer maxAscent = getMaxAscent(format.getFontName(), format.getFontsize());

        var response = pixelStorage//
                .computeIfAbsent(c, key -> new HashMap<>())//
                .computeIfAbsent(style, key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontColor(), key -> new HashMap<>())//
                .computeIfAbsent(format.getBackgroundColor(), key -> genDrawble(c, format, maxAscent))//
        ;
        return response.copy();
    }

    private Drawable genDrawble(Character c, TextFormat format, Integer maxAscent) {
        int[] pixels = fontGenerator.ofChar(c, format);
        return new DrawableImpl(pixels, new Size(pixels.length / maxAscent, maxAscent));
    }

    @Override
    public int getMaxAscent(String fontName, int fontSize) {
        return maxAscentStorage//
                .computeIfAbsent(fontName, key -> new HashMap<>())//
                .computeIfAbsent(fontSize, key -> fontGenerator.getMaxAscent(fontName, fontSize));
    }

}
