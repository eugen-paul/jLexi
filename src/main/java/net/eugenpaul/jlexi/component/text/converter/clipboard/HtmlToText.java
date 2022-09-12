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

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Slf4j
public class HtmlToText {

    private static final String STYLE_ATTR = "style";
    private static final String STYLE_TAG = "style";

    private static final List<String> CLIPBOARD_KEYWORDS = List.of(//
            "Version", //
            "StartHTML", //
            "EndHTML", //
            "StartFragment", //
            "EndFragment", //
            "StartSelection", //
            "EndSelection" //
    );

    private ResourceManager storage;
    private Document doc;

    @Setter
    private static HtmlFormatHelper defaultFormatHelper = new HtmlFormatHelperImpl();

    private HtmlFormatHelper currentFormatHelper;

    public HtmlToText(String clipboardHtml, ResourceManager storage) {
        var html = extractHtml(clipboardHtml);
        this.doc = Jsoup.parse(html);
        LOGGER.trace("HTML read:");
        LOGGER.trace(this.doc.outerHtml());

        this.storage = storage;
        this.currentFormatHelper = defaultFormatHelper;
    }

    private static CascadingStyleSheet readCss(String css) {
        return CSSReader.readFromString(css, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );
    }

    private static String extractHtml(String clipboardHtml) {
        int offset = 0;

        for (var tag : CLIPBOARD_KEYWORDS) {
            offset = readTag(clipboardHtml, offset, tag);
        }

        return clipboardHtml.substring(offset);
    }

    private static int readTag(String clipboardHtml, int offset, String tag) {
        if (clipboardHtml.startsWith(tag, offset)) {
            // tag
            offset += tag.length();
            // :
            offset++;
            // length
            while (isDigitOrDot(clipboardHtml.charAt(offset))) {
                offset++;
            }
            // EOL
            while (isEol(clipboardHtml.charAt(offset))) {
                offset++;
            }
        }
        return offset;
    }

    private static boolean isDigitOrDot(char c) {
        return c >= '0' && c <= '9' || c == '.';
    }

    private static boolean isEol(char c) {
        return c == '\n' || c == 'r';
    }

    public List<TextElement> convert() {
        var globalCss = readStyleTags();

        List<TextElement> response = new LinkedList<>();

        for (var element : this.doc.body().childNodes()) {
            parseChilds(globalCss, element, response, TextFormat.DEFAULT, TextFormatEffect.DEFAULT_FORMAT_EFFECT);
        }

        return response;
    }

    private CascadingStyleSheet readStyleTags() {
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

        format = this.currentFormatHelper.applyTagFormat( //
                node.nodeName(), //
                new AttrReadIterator<>(node.attributes().asList()), //
                format //
        );

        effect = this.currentFormatHelper.applyTagEffect(node.nodeName(), effect);

        if (node instanceof Element) {
            var element = (Element) node;
            format = this.currentFormatHelper.applyStyleTagAttr(element, globalCss, format);
            effect = this.currentFormatHelper.applyStyleTagAttr(element, globalCss, effect);
        }

        if (node.hasAttr(STYLE_ATTR)) {
            var tagStyle = node.attr(STYLE_ATTR);
            format = this.currentFormatHelper.applyStyleAttr(tagStyle, format);
            effect = this.currentFormatHelper.applyStyleAttr(tagStyle, effect);
        }

        for (Node child : node.childNodes()) {
            if (child instanceof TextNode) {
                textNodeToResponse((TextNode) child, response, format, effect);
            } else {
                parseChilds(globalCss, child, response, format, effect);
            }
        }

        if (node instanceof Element) {
            var element = (Element) node;
            if (element.tag().isBlock()) {
                response.add(TextElementFactory.genNewLineChar(this.storage, format, effect));
            }
        }
    }

    private void textNodeToResponse(TextNode node, List<TextElement> response, TextFormat format,
            TextFormatEffect formatEffect) {
        for (var c : node.getWholeText().toCharArray()) {
            var textChar = TextElementFactory.fromChar(this.storage, c, format, formatEffect);
            if (textChar != null) {
                response.add(textChar);
            }
        }
    }
}
