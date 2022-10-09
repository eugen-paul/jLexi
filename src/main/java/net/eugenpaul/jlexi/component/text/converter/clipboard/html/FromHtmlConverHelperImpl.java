package net.eugenpaul.jlexi.component.text.converter.clipboard.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;
import com.helger.css.utils.CSSNumberHelper;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class FromHtmlConverHelperImpl implements FromHtmlConvertHelper {

    private static final String ATTRIBUTE_FACE = "face";
    private static final String ATTRIBUTE_COLOR = "color";

    private static final String PROPERTY_BACKGROUND = "background";
    private static final String PROPERTY_BACKGROUND_COLOR = "background-color";
    private static final String PROPERTY_BORDER_BOTTOM = "border-bottom";
    private static final String PROPERTY_COLOR = "color";
    private static final String PROPERTY_FONT_FAMILY = "font-family";
    private static final String PROPERTY_FONT_SIZE = "font-size";
    private static final String PROPERTY_FONT_STYLE = "font-style";
    private static final String PROPERTY_FONT_WEIGHT = "font-weight";

    private static final String STYLE_ATTR = "style";

    @Override
    public Map<String, TreeMap<Integer, CSSDeclaration>> stylesProperties(Node node, CascadingStyleSheet globalCss) {

        Map<String, TreeMap<Integer, CSSDeclaration>> properties = new HashMap<>();

        addTagProperties(node.nodeName(), properties);

        if (node.nodeName().equals("font")) {
            addFontAttributes( //
                    properties, //
                    new AttrReadIterator<>(node.attributes().asList()) //
            );
        }

        if (node.hasAttr(STYLE_ATTR)) {
            var styleAttr = node.attr(STYLE_ATTR);
            addStyleAttr(styleAttr, properties);
        }

        if (node instanceof Element) {
            var element = (Element) node;
            addStyleTagAttr(element, globalCss.getAllStyleRules(), properties);
        }

        return properties;
    }

    private void addStyleTagAttr(//
            Element element, //
            ICommonsList<CSSStyleRule> rules, //
            Map<String, TreeMap<Integer, CSSDeclaration>> properties //
    ) {
        String tag = element.tagName();

        String[] classNames = element.className().split(" ");
        String id = element.id();

        for (var rule : rules) {
            for (var sel : rule.getAllSelectors()) {
                int cost = getCost(sel, tag, classNames, id);
                if (cost > 0) {
                    rule.getAllDeclarations().stream()//
                            .forEach(v -> properties.computeIfAbsent(v.getProperty(), k -> new TreeMap<>())//
                                    .put(cost, v)//
                            );
                }
            }
        }
    }

    private void addTagProperties(String tagName, Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        switch (tagName) {
        case "i":
            addItalic(properties);
            break;
        case "u":
            addUnderline(properties);
            break;
        case "b", "strong":
            addBold(properties);
            break;
        case "h1":
            addBold(properties);
            addSize(properties, 32);
            break;
        case "h2":
            addBold(properties);
            addSize(properties, 24);
            break;
        case "h3":
            addBold(properties);
            addSize(properties, 18);
            break;
        case "h4":
            addBold(properties);
            addSize(properties, 16);
            break;
        case "h5":
            addBold(properties);
            addSize(properties, 13);
            break;
        case "h6":
            addBold(properties);
            addSize(properties, 10);
            break;
        default:
            break;
        }
    }

    private void addBold(Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var decl = new CSSDeclaration(PROPERTY_FONT_WEIGHT, CSSExpression.createSimple("bold"));
        addDeclarationToProperties(properties, decl, 1);
    }

    private void addItalic(Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var decl = new CSSDeclaration(PROPERTY_FONT_STYLE, CSSExpression.createSimple("italic"));
        addDeclarationToProperties(properties, decl, 1);
    }

    private void addUnderline(Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var decl = new CSSDeclaration(PROPERTY_BORDER_BOTTOM, CSSExpression.createSimple("solid"));
        addDeclarationToProperties(properties, decl, 1);
    }

    private void addSize(Map<String, TreeMap<Integer, CSSDeclaration>> properties, int sizePx) {
        var decl = new CSSDeclaration(PROPERTY_FONT_SIZE, CSSExpression.createSimple(sizePx + "px"));
        addDeclarationToProperties(properties, decl, 1);
    }

    private void addFontAttributes(Map<String, TreeMap<Integer, CSSDeclaration>> properties,
            Iterator<Map.Entry<String, String>> attributesiterator) {
        while (attributesiterator.hasNext()) {
            var attr = attributesiterator.next();
            if (attr.getKey().equals(ATTRIBUTE_FACE)) {
                var fonts = attr.getValue().split(",");
                if (fonts.length > 0) {
                    var font = fonts[0];
                    if (font != null) {
                        var decl = new CSSDeclaration(PROPERTY_FONT_FAMILY, CSSExpression.createSimple(font));
                        addDeclarationToProperties(properties, decl, 10);
                    }
                }
            }

            var color = attr.getValue();
            if (attr.getKey().equals(ATTRIBUTE_COLOR) && color != null && HtmlColorHelper.isColor(color)) {
                var decl = new CSSDeclaration(PROPERTY_COLOR, CSSExpression.createSimple(color));
                addDeclarationToProperties(properties, decl, 10);
            }
        }
    }

    public void addStyleAttr(String styleAttr, Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        if (styleAttr == null) {
            return;
        }

        var declList = CSSReaderDeclarationList.readFromString(//
                styleAttr, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );

        if (declList == null) {
            return;
        }

        declList.stream()//
                .forEach(v -> addDeclarationToProperties(properties, v, 100));
    }

    private void addDeclarationToProperties(//
            Map<String, TreeMap<Integer, CSSDeclaration>> properties, //
            CSSDeclaration decl, //
            int cost //
    ) {
        properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                .put(cost, decl);
    }

    private int getCost(CSSSelector selector, String tag, String[] classNames, String id) {
        int cost = 0;

        if (selector.getAsCSSString().equals(tag)) {
            cost = 10;
        } else if (selector.getAsCSSString().equals("#" + id) //
        ) {
            cost = 1000;
        } else {
            for (var className : classNames) {
                if (selector.getAsCSSString().equals(tag + "." + className)) {
                    cost = 110;
                }
                if (selector.getAsCSSString().equals("." + className)) {
                    cost = 100;
                }
            }
        }

        // TODO check if combinator matches the element ("div p", "div > p", ...)

        return cost;
    }

    @Override
    public TextFormat applyFormatAttributes(TextFormat format,
            Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var font = bestDeclaration(properties, PROPERTY_FONT_FAMILY);
        format = applyFont(format, font);

        var size = bestDeclaration(properties, PROPERTY_FONT_SIZE);
        format = applyFontSize(format, size);

        var fontWeight = bestDeclaration(properties, PROPERTY_FONT_WEIGHT);
        format = applyFontWeight(format, fontWeight);

        var fontStyle = bestDeclaration(properties, PROPERTY_FONT_STYLE);
        format = applyFontStyle(format, fontStyle);

        var colorValue = bestDeclaration(properties, PROPERTY_COLOR);
        format = applyFontColor(format, colorValue);

        var bgColorValue = bestDeclaration(properties, PROPERTY_BACKGROUND_COLOR);
        format = applyBgColor(format, bgColorValue);

        var bgColorShortValue = bestDeclaration(properties, PROPERTY_BACKGROUND);
        format = applyBgColor(format, bgColorShortValue);
        return format;
    }

    @Override
    public TextFormatEffect applyEffectAttributes(TextFormatEffect effect,
            Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var font = bestDeclaration(properties, PROPERTY_BORDER_BOTTOM);
        effect = applyUnderline(effect, font);
        return effect;
    }

    private CSSDeclaration bestDeclaration(Map<String, TreeMap<Integer, CSSDeclaration>> properties,
            String propertyName) {
        var propertyMap = properties.get(propertyName);
        if (propertyMap == null || propertyMap.isEmpty()) {
            return null;
        }

        return propertyMap.lastEntry().getValue();
    }

    private TextFormat applyBgColor(TextFormat format, CSSDeclaration bgColorValue) {
        if (bgColorValue != null) {
            var bgColor = HtmlColorHelper.parseColor(bgColorValue.getExpression().getAsCSSString());
            if (bgColor != null) {
                format = format.withBackgroundColor(bgColor);
            }
        }
        return format;
    }

    private TextFormat applyFontColor(TextFormat format, CSSDeclaration colorValue) {
        if (colorValue != null) {
            var color = HtmlColorHelper.parseColor(colorValue.getExpression().getAsCSSString());
            if (color != null) {
                format = format.withFontColor(color);
            }
        }
        return format;
    }

    private TextFormat applyFont(TextFormat format, CSSDeclaration font) {
        if (font != null) {
            format = format.withFontName(CSSParseHelper.extractStringValue(font.getExpression().getAsCSSString()));
        }
        return format;
    }

    private TextFormat applyFontSize(TextFormat format, CSSDeclaration fontSize) {
        if (fontSize != null) {
            var size = CSSNumberHelper.getValueWithUnit(fontSize.getExpression().getAsCSSString());
            int px = TextFormat.DEFAULT.getFontsize();
            if (size != null) {
                switch (size.getUnit()) {
                case LENGTH_PT:
                    px = (int) (96.0 / 72.0 * size.getAsIntValue());
                    break;
                case PX:
                    px = size.getAsIntValue();
                    break;
                default:
                    break;
                }
                format = format.withFontsize(px);
            }
        }
        return format;
    }

    private TextFormat applyFontWeight(TextFormat format, CSSDeclaration fontWeight) {
        if (fontWeight != null) {
            var value = fontWeight.getExpression().getAsCSSString();
            switch (value) {
            case "normal":
                format = format.withBold(false);
                break;
            case "bold":
                format = format.withBold(true);
                break;
            default:
                break;
            }
        }
        return format;
    }

    private TextFormat applyFontStyle(TextFormat format, CSSDeclaration fontStyle) {
        if (fontStyle != null) {
            var value = fontStyle.getExpression().getAsCSSString();
            switch (value) {
            case "normal":
                format = format.withItalic(false);
                break;
            case "italic":
                format = format.withItalic(true);
                break;
            default:
                break;
            }
        }
        return format;
    }

    private TextFormatEffect applyUnderline(TextFormatEffect effect, CSSDeclaration font) {
        if (font != null) {
            var borderAttr = font.getExpression().getAsCSSString().split(" ");

            for (String attr : borderAttr) {
                if (attr.equals("solid")) {
                    effect = effect.withUnderline(FormatUnderlineType.SINGLE);
                } else if (attr.equals("double")) {
                    effect = effect.withUnderline(FormatUnderlineType.DOUBLE);
                } else if (HtmlColorHelper.isColor(attr)) {
                    var color = HtmlColorHelper.parseColor(attr);
                    if (color != null) {
                        effect = effect.withUnderlineColor(color);
                    }
                }
            }
        }
        return effect;
    }

    @Override
    public boolean isSizeBreakAfter(Node node, Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var isBreak = bestDeclaration(properties, "page-break-after");
        return checkIsBreak(isBreak);
    }

    @Override
    public boolean isSizeBreakBefore(Node node, Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var isBreak = bestDeclaration(properties, "page-break-before");
        return checkIsBreak(isBreak);
    }

    private boolean checkIsBreak(CSSDeclaration isBreak) {
        if (isBreak != null) {
            var breakValue = isBreak.getExpression().getAsCSSString();
            return breakValue.equals("always");
        }
        return false;
    }

}
