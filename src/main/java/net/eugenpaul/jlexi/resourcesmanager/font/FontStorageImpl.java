package net.eugenpaul.jlexi.resourcesmanager.font;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

/**
 * Storage for chars as PixelArray generated from FontGenerator
 */
public class FontStorageImpl extends FontStorage {

    private final FontPixelsGenerator fontGenerator;

    private Map<String, // FontName
            Map<Integer, // FontSize
                    Integer>> maxAscentStorage;

    private Map<String, // FontName
            Map<Integer, // FontSize
                    Integer>> ascentStorage;

    private Map<String, // FontName
            Map<Integer, // FontSize
                    Integer>> descentStorage;

    private Map<Character, // Character
            Map<Integer, // Style
                    Map<String, // FontName
                            Map<Integer, // Size
                                    Map<Color, // FontColor
                                            Map<Color, // BackgroundColor
                                                    Drawable>>>>>> drawableStorage;

    public FontStorageImpl(FontPixelsGenerator fontGenerator) {
        this.fontGenerator = fontGenerator;
        this.maxAscentStorage = new HashMap<>();
        this.ascentStorage = new HashMap<>();
        this.descentStorage = new HashMap<>();
        this.drawableStorage = new HashMap<>();
    }

    public FontStorageImpl() {
        this(new FontGenerator());
    }

    @Override
    public Drawable ofChar2(Character c, TextFormat format) {
        if (!CharacterHelper.isPrintable(c)) {
            return DEFAULT_DRAWABLE_2;
        }

        Integer style = fontGenerator.getStyle(format);

        return drawableStorage//
                .computeIfAbsent(c, key -> new HashMap<>())//
                .computeIfAbsent(style, key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontColor(), key -> new HashMap<>())//
                .computeIfAbsent(format.getBackgroundColor(), key -> fontGenerator.ofChar2(c, format))//
        ;
    }

    @Override
    public int getMaxAscent(String fontName, int fontSize) {
        return maxAscentStorage//
                .computeIfAbsent(fontName, key -> new HashMap<>())//
                .computeIfAbsent(fontSize, key -> fontGenerator.getMaxAscent(fontName, fontSize));
    }

    @Override
    public int getAscent(String fontName, int fontSize) {
        return ascentStorage//
                .computeIfAbsent(fontName, key -> new HashMap<>())//
                .computeIfAbsent(fontSize, key -> fontGenerator.getAscent(fontName, fontSize));
    }

    @Override
    public int getDescent(String fontName, int fontSize) {
        return descentStorage//
                .computeIfAbsent(fontName, key -> new HashMap<>())//
                .computeIfAbsent(fontSize, key -> fontGenerator.getDescent(fontName, fontSize));
    }

}
