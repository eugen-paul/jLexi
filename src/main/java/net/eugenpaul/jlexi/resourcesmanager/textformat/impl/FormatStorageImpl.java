package net.eugenpaul.jlexi.resourcesmanager.textformat.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.textformat.FormatterCreator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;
import net.eugenpaul.jlexi.utils.Color;

public class FormatStorageImpl implements FormatStorage {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatStorageImpl.class);

    private Map<Integer, // Style
            Map<String, // FontName
                    Map<Integer, // Size
                            Map<Color, // FontColor
                                    Map<Color, // BackgroundColor
                                            TextFormat>>>>> storage;

    private EnumMap<FormatterType, FormatterCreator> formatter;
    private Map<TextFormatEffect, TextFormatEffect> formatEffekts;

    public FormatStorageImpl() {
        storage = new HashMap<>();
        formatter = new EnumMap<>(FormatterType.class);
        formatEffekts = new HashMap<>();
        initFormatter();
    }

    private void initFormatter() {
        for (var type : FormatterType.values()) {
            formatter.put(type, type.getCreator());
        }
    }

    @Override
    public TextFormat add(@NonNull TextFormat format) {
        return storage//
                .computeIfAbsent(getStyle(format), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontColor(), key -> new HashMap<>())//
                .computeIfAbsent(format.getBackgroundColor(), key -> format);
    }

    private static int getStyle(TextFormat format) {
        return getStyle(format.getBold(), format.getItalic());
    }

    private static int getStyle(Boolean bold, Boolean italic) {
        return ((bold != null && bold.booleanValue()) ? 1 : 0)//
                | ((italic != null && italic.booleanValue()) ? 2 : 0)//
        ;
    }

    @Override
    public List<PixelsFormatter> formatter(TextFormatEffect format) {
        List<PixelsFormatter> response = new LinkedList<>();

        switch (format.getUnderline()) {
        case SINGLE:
            response.add(formatter.get(FormatterType.UNDERLINE_SINGLE).create(format.getUnderlineColor()));
            break;
        case DOUBLE:
            response.add(formatter.get(FormatterType.UNDERLINE_DOUBLE).create(format.getUnderlineColor()));
            break;
        default:
            break;
        }

        return response.stream()//
                .filter(Objects::nonNull)//
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public TextFormatEffect add(TextFormatEffect format) {
        return formatEffekts.computeIfAbsent(format, v -> v);
    }

}
