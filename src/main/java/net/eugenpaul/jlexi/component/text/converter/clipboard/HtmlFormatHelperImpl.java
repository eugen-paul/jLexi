package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
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

        format = applyFormatAttributes(format, declList::getAllDeclarationsOfPropertyName);

        return format;
    }

    @Override
    public TextFormat applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormat format) {
        String tag = element.tagName();

        String[] classNames = element.className().split(" ");
        String id = element.id();

        CSSStyleRule rule = computeBestRule(globalCss, tag, classNames, id);

        format = applyFormatAttributes(format, rule::getAllDeclarationsOfPropertyName);

        return format;
    }

    private CSSStyleRule computeBestRule(CascadingStyleSheet globalCss, String tag, String[] classNames, String id) {
        CSSStyleRule rule = new CSSStyleRule();

        // TODO How to choose the best style parameter?
        for (var st : globalCss.getAllStyleRules()) {
            for (var sel : st.getAllSelectors()) {
                if (sel.getAsCSSString().equals(tag) //
                        || sel.getAsCSSString().equals("#" + id) //
                ) {
                    for (var d : st.getAllDeclarations()) {
                        rule.addDeclaration(d);
                    }
                }
                for (var className : classNames) {
                    if (sel.getAsCSSString().equals(tag + "." + className)
                            || sel.getAsCSSString().equals("." + className)) {
                        for (var d : st.getAllDeclarations()) {
                            rule.addDeclaration(d);
                        }
                    }
                }
            }
        }
        return rule;
    }

    private TextFormat applyFormatAttributes(TextFormat format,
            Function<String, ICommonsList<CSSDeclaration>> getProperty) {
        var font = getProperty.apply("font-family");
        format = applyFont(format, font);

        var colorValue = getProperty.apply("color");
        format = applyFontColor(format, colorValue);

        var bgColorValue = getProperty.apply("background-color");
        format = applyBgColor(format, bgColorValue);

        var bgColorShortValue = getProperty.apply("background");
        format = applyBgColor(format, bgColorShortValue);
        return format;
    }

    private TextFormat applyBgColor(TextFormat format, ICommonsList<CSSDeclaration> bgColorValue) {
        if (bgColorValue.isNotEmpty()) {
            var bgColor = HtmlColorHelper
                    .parseColor(bgColorValue.get(bgColorValue.size() - 1).getExpression().getAsCSSString());
            if (bgColor != null) {
                format = format.withBackgroundColor(bgColor);
            }
        }
        return format;
    }

    private TextFormat applyFontColor(TextFormat format, ICommonsList<CSSDeclaration> colorValue) {
        if (colorValue.isNotEmpty()) {
            var color = HtmlColorHelper
                    .parseColor(colorValue.get(colorValue.size() - 1).getExpression().getAsCSSString());
            if (color != null) {
                format = format.withFontColor(color);
            }
        }
        return format;
    }

    private TextFormat applyFont(TextFormat format, ICommonsList<CSSDeclaration> font) {
        if (font.isNotEmpty()) {
            format = format
                    .withFontName(CSSParseHelper.extractStringValue(font.get(0).getExpression().getAsCSSString()));
        }
        return format;
    }

    private TextFormatEffect applyEffectAttributes(TextFormatEffect effect,
            Function<String, ICommonsList<CSSDeclaration>> getProperty) {
        var font = getProperty.apply("border-bottom");
        effect = applyUnderline(effect, font);
        return effect;
    }

    private TextFormatEffect applyUnderline(TextFormatEffect effect, ICommonsList<CSSDeclaration> font) {
        if (font.isNotEmpty()) {
            var borderAttr = font.get(0).getExpression().getAsCSSString().split(" ");

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
    public TextFormatEffect applyStyleAttr(Node node, String styleAttr, TextFormatEffect effect) {
        CSSDeclarationList declList = CSSReaderDeclarationList.readFromString(//
                styleAttr, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );

        if (declList == null) {
            return effect;
        }

        effect = applyEffectAttributes(effect, declList::getAllDeclarationsOfPropertyName);

        return effect;
    }

    @Override
    public TextFormatEffect applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormatEffect effect) {
        String tag = element.tagName();

        String[] classNames = element.className().split(" ");
        String id = element.id();

        CSSStyleRule rule = computeBestRule(globalCss, tag, classNames, id);

        effect = applyEffectAttributes(effect, rule::getAllDeclarationsOfPropertyName);

        return effect;
    }
}
