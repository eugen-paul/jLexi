package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Slf4j
public class HtmlToText {

    private static final String STYLE_TAG = "style";

    private ResourceManager storage;

    private FromHtmlConvertHelper currentFormatHelper;

    public HtmlToText(ResourceManager storage) {
        this.storage = storage;
        this.currentFormatHelper = new FromHtmlConverHelperImpl();
    }

    private static CascadingStyleSheet readCss(String css) {
        return CSSReader.readFromString(css, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );
    }

    public List<TextElement> convert(String html) {
        Document doc = Jsoup.parse(html);
        LOGGER.trace("HTML read:");
        LOGGER.trace(doc.outerHtml());

        var globalCss = readStyleTags(doc);

        List<TextElement> response = new LinkedList<>();

        for (var element : doc.body().childNodes()) {
            parseChilds(globalCss, element, response, TextFormat.DEFAULT, TextFormatEffect.DEFAULT_FORMAT_EFFECT);
        }

        return response;
    }

    private CascadingStyleSheet readStyleTags(Document doc) {
        List<CascadingStyleSheet> globalCss = new LinkedList<>();
        var styles = doc.getElementsByTag(STYLE_TAG);
        var iterator = styles.listIterator();
        while (iterator.hasNext()) {
            var css = readCss(iterator.next().html());
            if (css != null) {
                globalCss.add(css);
            }
        }

        return globalCss.stream().reduce(new CascadingStyleSheet(), HtmlToText::addRules);
    }

    /**
     * Copy all needed rules from source to target
     * 
     * @param target
     * @param source
     */
    private static CascadingStyleSheet addRules(CascadingStyleSheet target, CascadingStyleSheet source) {
        // We are only interested in the style rules.
        for (var rule : source.getAllStyleRules()) {
            target.addRule(rule);
        }
        return target;
    }

    private void parseChilds(CascadingStyleSheet globalCss, Node node, List<TextElement> response,
            TextFormat currentFormat, TextFormatEffect currentEffect) {
        TextFormat format = currentFormat;
        TextFormatEffect effect = currentEffect;

        var properties = this.currentFormatHelper.stylesProperties(node, globalCss);
        format = this.currentFormatHelper.applyFormatAttributes(format, properties);
        effect = this.currentFormatHelper.applyEffectAttributes(effect, properties);

        if (this.currentFormatHelper.isSizeBreakBefore(node, properties)) {
            response.add(TextElementFactory.genNewSectionChar(this.storage, format, effect));
        }

        for (Node child : node.childNodes()) {
            if (child instanceof TextNode) {
                textNodeToResponse((TextNode) child, response, format, effect);
            } else {
                // TODO should the current format be passed here or should the current properties be passed here?
                parseChilds(globalCss, child, response, format, effect);
            }
        }

        if (node instanceof Element) {
            var element = (Element) node;
            if (this.currentFormatHelper.isSizeBreakAfter(node, properties)) {
                response.add(TextElementFactory.genNewSectionChar(this.storage, format, effect));
            } else if (element.tag().isBlock()) {
                response.add(TextElementFactory.genNewLineChar(this.storage, format, effect));
            }
        }
    }

    private void textNodeToResponse(TextNode node, List<TextElement> response, TextFormat format,
            TextFormatEffect formatEffect) {
        for (var c : node.getWholeText().toCharArray()) {
            var textChar = TextElementFactory.fromChar(this.storage, c, format, formatEffect);
            if (textChar != null) {
                if (textChar.isEndOfLine()) {
                    textChar = TextElementFactory.fromChar(this.storage, ' ', format, formatEffect);
                }
                response.add(textChar);
            }
        }
    }
}
