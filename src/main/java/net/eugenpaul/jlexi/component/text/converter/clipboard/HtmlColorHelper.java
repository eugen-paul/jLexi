package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.regex.Pattern;

import com.helger.css.utils.CSSColorHelper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.utils.Color;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HtmlColorHelper {

    private static final String FORMAT_HEX = "^#([0-9a-fA-F]{3,4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$";
    private static final Pattern PATTERN_HEX = Pattern.compile(FORMAT_HEX);

    public static String toHexRGBColor(Color color) {
        return "#" + String.format("%02X%02X%02X", //
                color.getR() & 0xFF, //
                color.getG() & 0xFF, //
                color.getB() & 0xFF //
        );
    }

    public static boolean isColor(String value) {
        return isTextColor(value) //
                || isHexColor(value) //
                || isRgbaColor(value);
    }

    public static Color parseColor(String value) {

        if (isTextColor(value)) {
            return parseTextColor(value);
        }

        if (isHexColor(value)) {
            return parseHexColor(value);
        }

        if (isRgbaColor(value)) {
            return parseRgbaColor(value);
        }

        return null;
    }

    /**
     * Check if the passed String is valid text color value.
     * <p>
     * Example value:
     * <ul>
     * <li>red</li>
     * <li>brown</li>
     * </ul>
     * 
     * @param value
     * @return
     */
    public static boolean isTextColor(String value) {
        if (value == null) {
            return false;
        }

        try {
            Color.fromText(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse text-value to Color. The value must be a text color.
     * 
     * @param value - hex color {@link #isHexColor(String)}
     * @return Color or null
     */
    public static Color parseTextColor(String value) {
        try {
            return Color.fromText(value);
        } catch (Exception e) {
            LOGGER.trace("cann't parse text color " + value, e);
            return null;
        }
    }

    /**
     * Check if the passed String is valid CSS hex color value.
     * <p>
     * Example value:
     * <ul>
     * <li>RGB: #RRBBGG - #FF0000</li>
     * <li>RGBA: #RRBBGGAA - #FF000080</li>
     * <li>RGB: #RBG - #F00</li>
     * <li>RGBA: #RBGA - #F008</li>
     * </ul>
     * 
     * @param value
     * @return
     */
    public static boolean isHexColor(String value) {
        if (value == null) {
            return false;
        }
        return PATTERN_HEX.matcher(value).find();
    }

    /**
     * Parse hex-value to Color. The value must be a hex color.
     * 
     * @param value - hex color {@link #isHexColor(String)}
     * @return Color or null
     */
    public static Color parseHexColor(String value) {
        StringBuilder argbValue = new StringBuilder(10);
        argbValue.append("0x");

        switch (value.length()) {
        case 4:
            argbValue.append("FF");
            argbValue.append(value.charAt(1));
            argbValue.append(value.charAt(1));
            argbValue.append(value.charAt(2));
            argbValue.append(value.charAt(2));
            argbValue.append(value.charAt(3));
            argbValue.append(value.charAt(3));
            break;
        case 5:
            argbValue.append(value.charAt(4));
            argbValue.append(value.charAt(4));
            argbValue.append(value.charAt(1));
            argbValue.append(value.charAt(1));
            argbValue.append(value.charAt(2));
            argbValue.append(value.charAt(2));
            argbValue.append(value.charAt(3));
            argbValue.append(value.charAt(3));
            break;
        case 7:
            argbValue.append("FF");
            argbValue.append(value.substring(1));
            break;
        case 9:
            argbValue.append(value.substring(7));
            argbValue.append(value.substring(1, 7));
            break;
        default:
            break;
        }

        Color response = null;

        try {
            response = Color.fromHexArgb(argbValue.toString());
        } catch (Exception e) {
            LOGGER.trace("cann't parse hex color " + value, e);
        }

        return response;
    }

    /**
     * Check if the passed String is valid CSS RGB(A) color value.
     * <p>
     * Example value:
     * <ul>
     * <li>RGB: rgb(r,g,b)</li>
     * <li>RGBA: rgba(r,g,b,a)</li>
     * </ul>
     * 
     * @param value
     * @return
     */
    public static boolean isRgbaColor(String value) {
        if (value == null) {
            return false;
        }
        return CSSColorHelper.isRGBColorValue(value) || CSSColorHelper.isRGBAColorValue(value);
    }

    /**
     * Parse rgb(a)-value to Color. The value must be a rgb(a) color.
     * 
     * @param value - hex color {@link #isRgbaColor(String)}
     * @return Color or null
     */
    public static Color parseRgbaColor(String value) {
        var rgb = CSSColorHelper.getParsedRGBColorValue(value);
        if (rgb != null) {
            return new Color(//
                    Integer.parseInt(rgb.getRed()), //
                    Integer.parseInt(rgb.getGreen()), //
                    Integer.parseInt(rgb.getBlue()) //
            );
        }

        var rgba = CSSColorHelper.getParsedRGBAColorValue(value);
        if (rgba != null) {
            return new Color(//
                    Integer.parseInt(rgba.getRed()), //
                    Integer.parseInt(rgba.getGreen()), //
                    Integer.parseInt(rgba.getBlue()), //
                    Float.parseFloat(rgba.getOpacity()) //
            );
        }

        return null;
    }
}
