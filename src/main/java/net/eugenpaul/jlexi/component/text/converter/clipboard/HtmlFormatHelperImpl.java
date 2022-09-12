package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;
import net.eugenpaul.jlexi.utils.Color;

public class HtmlFormatHelperImpl implements HtmlFormatHelper {

    @Override
    public TextFormat applyTagFormat( //
            String tagName, //
            Iterator<Map.Entry<String, String>> attributesiterator, //
            TextFormat currentFormat //
    ) {
        TextFormat format = currentFormat;

        switch (tagName) {
        case "i":
            format = format.withItalic(true);
            break;
        case "b", "strong":
            format = format.withBold(true);
            break;
        case "h1":
            format = format.withBold(true);
            format = format.withFontsize(32);
            break;
        case "h2":
            format = format.withBold(true);
            format = format.withFontsize(24);
            break;
        case "h3":
            format = format.withBold(true);
            format = format.withFontsize(18);
            break;
        case "h4":
            format = format.withBold(true);
            format = format.withFontsize(16);
            break;
        case "h5":
            format = format.withBold(true);
            format = format.withFontsize(13);
            break;
        case "h6":
            format = format.withBold(true);
            format = format.withFontsize(10);
            break;
        case "font":
            format = applyFontTagArrtibutes(attributesiterator, format);
            break;
        default:
            break;
        }

        return format;
    }

    private TextFormat applyFontTagArrtibutes(Iterator<Map.Entry<String, String>> attributesiterator,
            TextFormat format) {
        while (attributesiterator.hasNext()) {
            var attr = attributesiterator.next();
            if (attr.getKey().equals("face")) {
                var fonts = attr.getValue().split(",");
                if (fonts.length > 0) {
                    format = format.withFontName(fonts[0]);
                }
            }
            if (attr.getKey().equals("color")) {
                var color = HtmlColorHelper.parseColor(attr.getValue());
                if (color != null) {
                    format = format.withFontColor(color);
                }
            }
        }
        return format;
    }

    @Override
    public TextFormatEffect applyTagEffect(String tagName, TextFormatEffect currentEffect) {
        TextFormatEffect effect = currentEffect;

        switch (tagName) {
        case "u":
            effect = effect.withUnderlineColor(Color.BLACK);
            break;
        default:
            break;
        }

        return effect;
    }

    @Override
    public TextFormat applyStyleAttr(String styleAttr, TextFormat format) {
        CSSDeclarationList declList = CSSReaderDeclarationList.readFromString(//
                styleAttr, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );

        if (declList == null) {
            return format;
        }

        Map<String, TreeMap<Integer, CSSDeclaration>> response = new HashMap<>();

        declList.stream()//
                .forEach(v -> response.computeIfAbsent(v.getProperty(), k -> new TreeMap<>())//
                        .put(1, v)//
                );

        format = applyFormatAttributes(format, response);

        return format;
    }

    @Override
    public TextFormat applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormat format) {
        var properties = getBestAttribute(globalCss.getAllStyleRules(), element);
        return applyFormatAttributes(format, properties);
    }

    private Map<String, TreeMap<Integer, CSSDeclaration>> getBestAttribute(//
            ICommonsList<CSSStyleRule> rules, //
            Element element //
    ) {
        Map<String, TreeMap<Integer, CSSDeclaration>> response = new HashMap<>();

        String tag = element.tagName();

        String[] classNames = element.className().split(" ");
        String id = element.id();

        for (var rule : rules) {
            for (var sel : rule.getAllSelectors()) {
                int cost = getCost(sel, tag, classNames, id);
                if (cost > 0) {
                    rule.getAllDeclarations().stream()//
                            .forEach(v -> response.computeIfAbsent(v.getProperty(), k -> new TreeMap<>())//
                                    .put(cost, v)//
                            );
                }
            }
        }

        return response;
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

    private TextFormat applyFormatAttributes(TextFormat format,
            Map<String, TreeMap<Integer, CSSDeclaration>> properties) {
        var font = bestDeclaration(properties, "font-family");
        format = applyFont(format, font);

        var colorValue = bestDeclaration(properties, "color");
        format = applyFontColor(format, colorValue);

        var bgColorValue = bestDeclaration(properties, "background-color");
        format = applyBgColor(format, bgColorValue);

        var bgColorShortValue = bestDeclaration(properties, "background");
        format = applyBgColor(format, bgColorShortValue);
        return format;
    }

    private TextFormatEffect applyEffectAttributes(TextFormatEffect effect,
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
    public TextFormatEffect applyStyleAttr(String styleAttr, TextFormatEffect effect) {
        CSSDeclarationList declList = CSSReaderDeclarationList.readFromString(//
                styleAttr, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );

        if (declList == null) {
            return effect;
        }

        Map<String, TreeMap<Integer, CSSDeclaration>> response = new HashMap<>();

        declList.stream()//
                .forEach(v -> response.computeIfAbsent(v.getProperty(), k -> new TreeMap<>())//
                        .put(1, v)//
                );

        effect = applyEffectAttributes(effect, response);

        return effect;
    }

    @Override
    public TextFormatEffect applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormatEffect effect) {
        var properties = getBestAttribute(globalCss.getAllStyleRules(), element);
        return applyEffectAttributes(effect, properties);
    }
}
