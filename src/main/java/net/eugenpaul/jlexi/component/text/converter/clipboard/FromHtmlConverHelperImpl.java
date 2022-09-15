package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class FromHtmlConverHelperImpl implements FromHtmlConvertHelper {

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
        CSSDeclaration decl = new CSSDeclaration("font-weight", CSSExpression.createSimple("bold"));
        properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                .put(1, decl);
    }

    private void addItalic(Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        CSSDeclaration decl = new CSSDeclaration("font-style", CSSExpression.createSimple("italic"));
        properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                .put(1, decl);
    }

    private void addUnderline(Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        CSSDeclaration decl = new CSSDeclaration("border-bottom", CSSExpression.createSimple("solid"));
        properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                .put(1, decl);
    }

    private void addSize(Map<String, TreeMap<Integer, CSSDeclaration>> properties, int sizePx) {
        CSSDeclaration decl = new CSSDeclaration("font-size", CSSExpression.createSimple(sizePx + "px"));
        properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                .put(1, decl);
    }

    private void addFontAttributes(Map<String, TreeMap<Integer, CSSDeclaration>> properties,
            Iterator<Map.Entry<String, String>> attributesiterator) {
        while (attributesiterator.hasNext()) {
            var attr = attributesiterator.next();
            if (attr.getKey().equals("face")) {
                var fonts = attr.getValue().split(",");
                if (fonts.length > 0) {
                    CSSDeclaration decl = new CSSDeclaration("font-family", CSSExpression.createSimple(fonts[0]));
                    properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                            .put(10, decl);
                }
            }
            if (attr.getKey().equals("color") && HtmlColorHelper.isColor(attr.getValue())) {
                CSSDeclaration decl = new CSSDeclaration("color", CSSExpression.createSimple(attr.getValue()));
                properties.computeIfAbsent(decl.getProperty(), v -> new TreeMap<>())//
                        .put(10, decl);
            }
        }
    }

    public void addStyleAttr(String styleAttr, Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        CSSDeclarationList declList = CSSReaderDeclarationList.readFromString(//
                styleAttr, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );

        if (declList == null) {
            return;
        }

        declList.stream()//
                .forEach(v -> properties.computeIfAbsent(v.getProperty(), k -> new TreeMap<>())//
                        .put(100, v)//
                );
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
        var font = bestDeclaration(properties, "font-family");
        format = applyFont(format, font);

        var fontWeight = bestDeclaration(properties, "font-weight");
        format = applyFontWeight(format, fontWeight);

        var fontStyle = bestDeclaration(properties, "font-style");
        format = applyFontStyle(format, fontStyle);

        var colorValue = bestDeclaration(properties, "color");
        format = applyFontColor(format, colorValue);

        var bgColorValue = bestDeclaration(properties, "background-color");
        format = applyBgColor(format, bgColorValue);

        var bgColorShortValue = bestDeclaration(properties, "background");
        format = applyBgColor(format, bgColorShortValue);
        return format;
    }

    @Override
    public TextFormatEffect applyEffectAttributes(TextFormatEffect effect,
            Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var font = bestDeclaration(properties, "border-bottom");
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

}
