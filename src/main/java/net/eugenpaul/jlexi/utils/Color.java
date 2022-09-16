package net.eugenpaul.jlexi.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode
@Slf4j
public final class Color {
    private static final String EXCEPTION_TEXT = "unsuported Format ";

    private static final Map<String, Color> stringToColor = Map.ofEntries(//
            Map.entry("aliceblue", new Color(0xFFF0F8FF)), //
            Map.entry("antiquewhite", new Color(0xFFFAEBD7)), //
            Map.entry("aquamarine", new Color(0xFF7FFFD4)), //
            Map.entry("azure", new Color(0xFFF0FFFF)), //
            Map.entry("beige", new Color(0xFFF5F5DC)), //
            Map.entry("bisque", new Color(0xFFFFE4C4)), //
            Map.entry("black", new Color(0xFF000000)), //
            Map.entry("blanchedalmond", new Color(0xFFFFEBCD)), //
            Map.entry("blue", new Color(0xFF0000FF)), //
            Map.entry("blueviolet", new Color(0xFF8A2BE2)), //
            Map.entry("brown", new Color(0xFFA52A2A)), //
            Map.entry("burlywood", new Color(0xFFDEB887)), //
            Map.entry("cadetblue", new Color(0xFF5F9EA0)), //
            Map.entry("chartreuse", new Color(0xFF7FFF00)), //
            Map.entry("chocolate", new Color(0xFFD2691E)), //
            Map.entry("coral", new Color(0xFFFF7F50)), //
            Map.entry("cornflowerblue", new Color(0xFF6495ED)), //
            Map.entry("cornsilk", new Color(0xFFFFF8DC)), //
            Map.entry("crimson", new Color(0xFFDC143C)), //
            Map.entry("cyan", new Color(0xFF00FFFF)), //
            Map.entry("darkblue", new Color(0xFF00008B)), //
            Map.entry("darkcyan", new Color(0xFF008B8B)), //
            Map.entry("darkgoldenrod", new Color(0xFFB8860B)), //
            Map.entry("darkgray", new Color(0xFFA9A9A9)), //
            Map.entry("darkgreen", new Color(0xFF006400)), //
            Map.entry("darkkhaki", new Color(0xFFBDB76B)), //
            Map.entry("darkmagenta", new Color(0xFF8B008B)), //
            Map.entry("darkolivegreen", new Color(0xFF556B2F)), //
            Map.entry("darkorange", new Color(0xFFFF8C00)), //
            Map.entry("darkorchid", new Color(0xFF9932CC)), //
            Map.entry("darkred", new Color(0xFF8B0000)), //
            Map.entry("darksalmon", new Color(0xFFE9967A)), //
            Map.entry("darkseagreen", new Color(0xFF8FBC8F)), //
            Map.entry("darkslateblue", new Color(0xFF483D8B)), //
            Map.entry("darkslategray", new Color(0xFF2F4F4F)), //
            Map.entry("darkturquoise", new Color(0xFF00CED1)), //
            Map.entry("darkviolet", new Color(0xFF9400D3)), //
            Map.entry("deeppink", new Color(0xFFFF1493)), //
            Map.entry("deepskyblue", new Color(0xFF00BFFF)), //
            Map.entry("dimgray", new Color(0xFF696969)), //
            Map.entry("dodgerblue", new Color(0xFF1E90FF)), //
            Map.entry("firebrick", new Color(0xFFB22222)), //
            Map.entry("floralwhite", new Color(0xFFFFFAF0)), //
            Map.entry("forestgreen", new Color(0xFF228B22)), //
            Map.entry("gainsboro", new Color(0xFFDCDCDC)), //
            Map.entry("ghostwhite", new Color(0xFFF8F8FF)), //
            Map.entry("gold", new Color(0xFFFFD700)), //
            Map.entry("goldenrod", new Color(0xFFDAA520)), //
            Map.entry("grey", new Color(0xFF808080)), //
            Map.entry("gray", new Color(0xFF808080)), //
            Map.entry("green", new Color(0xFF008000)), //
            Map.entry("greenyellow", new Color(0xFFADFF2F)), //
            Map.entry("honeydew", new Color(0xFFF0FFF0)), //
            Map.entry("hotpink", new Color(0xFFFF69B4)), //
            Map.entry("indianred ", new Color(0xFFCD5C5C)), //
            Map.entry("indigo ", new Color(0xFF4B0082)), //
            Map.entry("ivory", new Color(0xFFFFFFF0)), //
            Map.entry("khaki", new Color(0xFFF0E68C)), //
            Map.entry("lavender", new Color(0xFFE6E6FA)), //
            Map.entry("lavenderblush", new Color(0xFFFFF0F5)), //
            Map.entry("lawngreen", new Color(0xFF7CFC00)), //
            Map.entry("lemonchiffon", new Color(0xFFFFFACD)), //
            Map.entry("lightblue", new Color(0xFFADD8E6)), //
            Map.entry("lightcoral", new Color(0xFFF08080)), //
            Map.entry("lightcyan", new Color(0xFFE0FFFF)), //
            Map.entry("lightgoldenrodyellow", new Color(0xFFFAFAD2)), //
            Map.entry("lightgray", new Color(0xFFD3D3D3)), //
            Map.entry("lightgreen", new Color(0xFF90EE90)), //
            Map.entry("lightpink", new Color(0xFFFFB6C1)), //
            Map.entry("lightsalmon", new Color(0xFFFFA07A)), //
            Map.entry("lightseagreen", new Color(0xFF20B2AA)), //
            Map.entry("lightskyblue", new Color(0xFF87CEFA)), //
            Map.entry("lightslategray", new Color(0xFF778899)), //
            Map.entry("lightsteelblue", new Color(0xFFB0C4DE)), //
            Map.entry("lightyellow", new Color(0xFFFFFFE0)), //
            Map.entry("lime", new Color(0xFF00FF00)), //
            Map.entry("limegreen", new Color(0xFF32CD32)), //
            Map.entry("linen", new Color(0xFFFAF0E6)), //
            Map.entry("magenta", new Color(0xFFFF00FF)), //
            Map.entry("maroon", new Color(0xFF800000)), //
            Map.entry("mediumaquamarine", new Color(0xFF66CDAA)), //
            Map.entry("mediumblue", new Color(0xFF0000CD)), //
            Map.entry("mediumorchid", new Color(0xFFBA55D3)), //
            Map.entry("mediumpurple", new Color(0xFF9370DB)), //
            Map.entry("mediumseagreen", new Color(0xFF3CB371)), //
            Map.entry("mediumslateblue", new Color(0xFF7B68EE)), //
            Map.entry("mediumspringgreen", new Color(0xFF00FA9A)), //
            Map.entry("mediumturquoise", new Color(0xFF48D1CC)), //
            Map.entry("mediumvioletred", new Color(0xFFC71585)), //
            Map.entry("midnightblue", new Color(0xFF191970)), //
            Map.entry("mintcream", new Color(0xFFF5FFFA)), //
            Map.entry("mistyrose", new Color(0xFFFFE4E1)), //
            Map.entry("moccasin", new Color(0xFFFFE4B5)), //
            Map.entry("navajowhite", new Color(0xFFFFDEAD)), //
            Map.entry("navy", new Color(0xFF000080)), //
            Map.entry("oldlace", new Color(0xFFFDF5E6)), //
            Map.entry("olive", new Color(0xFF808000)), //
            Map.entry("olivedrab", new Color(0xFF6B8E23)), //
            Map.entry("orange", new Color(0xFFFFA500)), //
            Map.entry("orangered", new Color(0xFFFF4500)), //
            Map.entry("orchid", new Color(0xFFDA70D6)), //
            Map.entry("palegoldenrod", new Color(0xFFEEE8AA)), //
            Map.entry("palegreen", new Color(0xFF98FB98)), //
            Map.entry("paleturquoise", new Color(0xFFAFEEEE)), //
            Map.entry("palevioletred", new Color(0xFFDB7093)), //
            Map.entry("papayawhip", new Color(0xFFFFEFD5)), //
            Map.entry("peachpuff", new Color(0xFFFFDAB9)), //
            Map.entry("peru", new Color(0xFFCD853F)), //
            Map.entry("pink", new Color(0xFFFFC0CB)), //
            Map.entry("plum", new Color(0xFFDDA0DD)), //
            Map.entry("powderblue", new Color(0xFFB0E0E6)), //
            Map.entry("purple", new Color(0xFF800080)), //
            Map.entry("rebeccapurple", new Color(0xFF663399)), //
            Map.entry("red", new Color(0xFFFF0000)), //
            Map.entry("rosybrown", new Color(0xFFBC8F8F)), //
            Map.entry("royalblue", new Color(0xFF4169E1)), //
            Map.entry("saddlebrown", new Color(0xFF8B4513)), //
            Map.entry("salmon", new Color(0xFFFA8072)), //
            Map.entry("sandybrown", new Color(0xFFF4A460)), //
            Map.entry("seagreen", new Color(0xFF2E8B57)), //
            Map.entry("seashell", new Color(0xFFFFF5EE)), //
            Map.entry("sienna", new Color(0xFFA0522D)), //
            Map.entry("silver", new Color(0xFFC0C0C0)), //
            Map.entry("skyblue", new Color(0xFF87CEEB)), //
            Map.entry("slateblue", new Color(0xFF6A5ACD)), //
            Map.entry("slategray", new Color(0xFF708090)), //
            Map.entry("snow", new Color(0xFFFFFAFA)), //
            Map.entry("springgreen", new Color(0xFF00FF7F)), //
            Map.entry("steelblue", new Color(0xFF4682B4)), //
            Map.entry("tan", new Color(0xFFD2B48C)), //
            Map.entry("teal", new Color(0xFF008080)), //
            Map.entry("thistle", new Color(0xFFD8BFD8)), //
            Map.entry("tomato", new Color(0xFFFF6347)), //
            Map.entry("turquoise", new Color(0xFF40E0D0)), //
            Map.entry("violet", new Color(0xFFEE82EE)), //
            Map.entry("wheat", new Color(0xFFF5DEB3)), //
            Map.entry("white", new Color(0xFFFFFFFF)), //
            Map.entry("whitesmoke", new Color(0xFFF5F5F5)), //
            Map.entry("yellow", new Color(0xFFFFFF00)), //
            Map.entry("yellowgreen", new Color(0xFF9ACD32)) //
    );

    public static final Color WHITE = stringToColor.get("white");
    public static final Color BLACK = stringToColor.get("black");
    public static final Color GREY = stringToColor.get("grey");
    public static final Color RED = stringToColor.get("red");
    public static final Color GREEN = stringToColor.get("green");
    public static final Color YELLOW = stringToColor.get("yellow");
    public static final Color BLUE = stringToColor.get("blue");
    public static final Color INVISIBLE = new Color(0x00_00_00_00);

    private static final Pattern HEX_PATTERN = Pattern
            .compile("^0x([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})_?([0-9a-fA-F]{2})$");

    private final int valueArgb;
    private final int valueRgbA;

    public Color(int valueArgb) {
        this.valueArgb = valueArgb;
        this.valueRgbA = valueArgb << 8 | valueArgb >>> 24;
    }

    /**
     * Constructor
     * 
     * @param r - red value [0,255]
     * @param g - green value [0,255]
     * @param b - blue value [0,255]
     */
    public Color(int r, int g, int b) {
        this.valueArgb = 0xFF << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
        this.valueRgbA = valueArgb << 8 | valueArgb >>> 24;
    }

    /**
     * Constructor
     * 
     * @param r - red value [0,255]
     * @param g - green value [0,255]
     * @param b - blue value [0,255]
     * @param a - alpha value [0,255]
     */
    public Color(int r, int g, int b, int a) {
        this.valueArgb = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
        this.valueRgbA = valueArgb << 8 | valueArgb >>> 24;
    }

    /**
     * Constructor
     * 
     * @param r - red value [0,255]
     * @param g - green value [0,255]
     * @param b - blue value [0,255]
     * @param o - opacity value [0,1]
     */
    public Color(int r, int g, int b, float o) {
        int a = (int) (o * 0xFF / 1f);
        this.valueArgb = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
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

    public String getHexRgba() {
        return String.format("0x%02X_%02X_%02X_%02X", //
                (valueArgb >>> 16) & 0xFF, //
                (valueArgb >>> 8) & 0xFF, //
                valueArgb & 0xFF, //
                valueArgb >>> 24 //
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

    public static boolean isTextColor(String input) {
        return stringToColor.containsKey(input.toLowerCase());
    }

    public static Color fromText(String input) throws IllegalArgumentException {
        var response = stringToColor.get(input.toLowerCase());
        if (response != null) {
            return response;
        }
        throw new IllegalArgumentException(EXCEPTION_TEXT + input);
    }

    /**
     * Parse Color from HEX-Value. Example: 0xFF010203
     * 
     * @param input
     * @return
     * @throws IllegalArgumentException
     */
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
