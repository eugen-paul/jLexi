package net.eugenpaul.jlexi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public final class Color {
    private static final String EXCEPTION_TEXT = "unsuported Format ";

    public static final Color WHITE = new Color(0xFF_FF_FF_FF);
    public static final Color BLACK = new Color(0xFF_00_00_00);
    public static final Color RED = new Color(0xFF_FF_00_00);
    public static final Color GREEN = new Color(0xFF_00_FF_00);
    public static final Color BLUE = new Color(0xFF_00_00_FF);
    public static final Color INVISIBLE = new Color(0x00_00_00_00);

    private static final Logger LOGGER = LoggerFactory.getLogger(Color.class);

    private static final Pattern HEX_ARGB = Pattern
            .compile("^0x([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})$");

    private final int value;

    public int getARGB() {
        return value;
    }

    public String getHexARGB() {
        return String.format("0x%02X_%02X_%02X_%02X", //
                value >>> 24, //
                (value >>> 16) & 0xFF, //
                (value >>> 8) & 0xFF, //
                value & 0xFF //
        );
    }

    public int getA() {
        return (value & 0xFF_00_00_00) >>> 24;
    }

    public int getR() {
        return (value & 0x00_FF_00_00) >>> 16;
    }

    public int getG() {
        return (value & 0x00_00_FF_00) >>> 8;
    }

    public int getB() {
        return (value & 0x00_00_00_FF);
    }

    public static Color convert(String input) throws IllegalArgumentException {
        try {
            return fromHexARGB(input);
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
        switch (input.toLowerCase()) {
        case "black":
            return Color.BLACK;
        case "white":
            return Color.WHITE;
        case "red":
            return Color.RED;
        case "green":
            return Color.GREEN;
        case "blue":
            return Color.BLUE;
        default:
            break;
        }
        throw new IllegalArgumentException(EXCEPTION_TEXT + input);
    }

    public static Color fromHexARGB(String input) throws IllegalArgumentException {
        Matcher matcher = HEX_ARGB.matcher(input);
        if (matcher.find()) {
            Long value = Long.parseLong(matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4), 16);
            return new Color(value.intValue());
        } else {
            throw new IllegalArgumentException(EXCEPTION_TEXT + input);
        }
    }
}
