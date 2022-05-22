package net.eugenpaul.jlexi.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Color {
    private static final String EXCEPTION_TEXT = "unsuported Format ";

    public static final Color WHITE = new Color(0xFF_FF_FF_FF);
    public static final Color BLACK = new Color(0xFF_00_00_00);
    public static final Color GREY = new Color(0xFF_BE_BE_BE);
    public static final Color RED = new Color(0xFF_FF_00_00);
    public static final Color GREEN = new Color(0xFF_00_FF_00);
    public static final Color YELLOW = new Color(0xFF_FF_FF_00);
    public static final Color BLUE = new Color(0xFF_00_00_FF);
    public static final Color INVISIBLE = new Color(0x00_00_00_00);

    private static final Map<String, Color> stringToColor = Map.ofEntries(//
            Map.entry("white", WHITE), //
            Map.entry("black", BLACK), //
            Map.entry("grey", GREY), //
            Map.entry("red", RED), //
            Map.entry("green", GREEN), //
            Map.entry("yellow", YELLOW), //
            Map.entry("blue", BLUE), //
            Map.entry("invisible", INVISIBLE)//
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(Color.class);

    private static final Pattern HEX_PATTERN = Pattern
            .compile("^0x([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})$");

    private final int valueArgb;
    private final int valueRgbA;

    public Color(int valueArgb) {
        this.valueArgb = valueArgb;
        this.valueRgbA = valueArgb << 8 | valueArgb >>> 24;
    }

    public int getArgb() {
        return valueArgb;
    }

    public int getRgba() {
        return valueRgbA;
    }

    public String getHexArgb() {
        return String.format("0x%02X_%02X_%02X_%02X", //
                valueArgb >>> 24, //
                (valueArgb >>> 16) & 0xFF, //
                (valueArgb >>> 8) & 0xFF, //
                valueArgb & 0xFF //
        );
    }

    public int getA() {
        return (valueArgb & 0xFF_00_00_00) >>> 24;
    }

    public int getR() {
        return (valueArgb & 0x00_FF_00_00) >>> 16;
    }

    public int getG() {
        return (valueArgb & 0x00_00_FF_00) >>> 8;
    }

    public int getB() {
        return (valueArgb & 0x00_00_00_FF);
    }

    public static Color convert(String input) throws IllegalArgumentException {
        try {
            return fromHexArgb(input);
        } catch (Exception e) {
            LOGGER.trace("not ARGB", e);
        }

        try {
            return fromText(input);
        } catch (Exception e) {
            LOGGER.trace("not Text", e);
        }

        throw new IllegalArgumentException(EXCEPTION_TEXT + input);
    }

    public static Color fromText(String input) throws IllegalArgumentException {
        var response = stringToColor.get(input.toLowerCase());
        if (response != null) {
            return response;
        }
        throw new IllegalArgumentException(EXCEPTION_TEXT + input);
    }

    public static Color fromHexArgb(String input) throws IllegalArgumentException {
        Matcher matcher = HEX_PATTERN.matcher(input);
        if (matcher.find()) {
            Long argb = Long.parseLong(matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4), 16);
            return new Color(argb.intValue());
        } else {
            throw new IllegalArgumentException(EXCEPTION_TEXT + input);
        }
    }

    public static Color fromHexRgba(String input) throws IllegalArgumentException {
        Matcher matcher = HEX_PATTERN.matcher(input);
        if (matcher.find()) {
            Long argb = Long.parseLong(matcher.group(4) + matcher.group(1) + matcher.group(2) + matcher.group(3), 16);
            return new Color(argb.intValue());
        } else {
            throw new IllegalArgumentException(EXCEPTION_TEXT + input);
        }
    }

    public boolean isInvisible() {
        return (valueRgbA & 0xFF) == 0;
    }
}
