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

public class FormatStorageImpl implements FormatStorage {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatStorageImpl.class);

    private Map<Integer, // Style
            Map<String, // FontName
                    Map<Integer, // Size
                            TextFormat>>> storage;

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
                .computeIfAbsent(format.getFontsize(), key -> format);
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
    public TextFormat setFontSize(@NonNull TextFormat format, int size) {
        return storage//
                .computeIfAbsent(getStyle(format), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(size, key -> //
                TextFormat.builder()//
                        .fontName(format.getFontName())//
                        .fontsize(size)//
                        .bold(format.getBold())//
                        .italic(format.getItalic())//
                        .build()//
                );
    }

    @Override
    public TextFormat setFontName(@NonNull TextFormat format, @NonNull String fontName) {
        return storage//
                .computeIfAbsent(getStyle(format), key -> new HashMap<>())//
                .computeIfAbsent(fontName, key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> //
                TextFormat.builder()//
                        .fontName(fontName)//
                        .fontsize(format.getFontsize())//
                        .bold(format.getBold())//
                        .italic(format.getItalic())//
                        .build()//
                );
    }

    @Override
    public TextFormat setBold(@NonNull TextFormat format, boolean bold) {
        return storage//
                .computeIfAbsent(getStyle(bold, format.getItalic()), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> //
                TextFormat.builder()//
                        .fontName(format.getFontName())//
                        .fontsize(format.getFontsize())//
                        .bold(bold)//
                        .italic(format.getItalic())//
                        .build()//
                );
    }

    @Override
    public TextFormat setItalic(@NonNull TextFormat format, boolean italic) {
        return storage//
                .computeIfAbsent(getStyle(format.getBold(), italic), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontName(), key -> new HashMap<>())//
                .computeIfAbsent(format.getFontsize(), key -> //
                TextFormat.builder()//
                        .fontName(format.getFontName())//
                        .fontsize(format.getFontsize())//
                        .bold(format.getBold())//
                        .italic(italic)//
                        .build()//
                );
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
