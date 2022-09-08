package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.decl.visit.ICSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class HtmlToText {

    private static final List<String> TAGS = List.of(//
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

    public HtmlToText(String clipboardHtml, ResourceManager storage) {
        var html = getHtml(clipboardHtml);
        this.doc = Jsoup.parse(html);
        System.out.println(html);
        System.out.println("---------------------------------");
        System.out.println(this.doc.outerHtml());
        System.out.println("---------------------------------");

        this.storage = storage;
    }

    private static CascadingStyleSheet readCss(String css) {
        return CSSReader.readFromString(css, //
                ECSSVersion.CSS30, //
                new DoNothingCSSParseErrorHandler() //
        );
    }

    private static String getHtml(String clipboardHtml) {

        int offset = 0;

        for (var tag : TAGS) {
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
        List<CascadingStyleSheet> globalCss = new LinkedList<>();

        var styles = doc.getElementsByTag("style");
        var iterator = styles.listIterator();
        while (iterator.hasNext()) {
            var css = readCss(iterator.next().html());
            if (css != null) {
                globalCss.add(css);
            }
        }

        List<TextElement> response = new LinkedList<>();

        for (var element : this.doc.body().childNodes()) {
            printChilds(globalCss, element, response, TextFormat.DEFAULT, TextFormatEffect.DEFAULT_FORMAT_EFFECT);
        }

        return response;
    }

    private void printChilds(List<CascadingStyleSheet> globalCss, Node node, List<TextElement> response,
            TextFormat currentFormat, TextFormatEffect currentEffect) {
        TextFormat format = currentFormat;
        TextFormatEffect effect = currentEffect;

        switch (node.nodeName()) {
        case "i":
            format = format.withItalic(true);
            break;
        case "b":
            format = format.withBold(true);
            break;
        default:
            break;
        }

        if (node instanceof Element) {
            var element = (Element) node;
            format = getFormat(element, globalCss, format);
            effect = getFormat(element, globalCss, effect);
        }

        for (Node child : node.childNodes()) {
            if (child instanceof TextNode) {
                textNodeToResponse((TextNode) child, response, format, effect);
            } else {
                printChilds(globalCss, child, response, format, effect);
            }
        }

        if (node instanceof Element) {
            var element = (Element) node;
            if (element.tag().isBlock()) {
                response.add(TextElementFactory.genNewLineChar(this.storage, format, effect));
            }
        }
    }

    private TextFormat getFormat(Element element, List<CascadingStyleSheet> globalCss, TextFormat format) {
        String tag = element.tagName();

        String[] classNames = element.className().split(" ");
        String id = element.id();

        for (var css : globalCss) {
            var styles = css.getAllStyleRules().getAll(v -> {
                for (var d : v.getAllSelectors()) {
                    if (d.equals(tag)) {
                        return true;
                    }
                    if (d.equals(tag) || d.equals(classNames) || d.equals(id)) {
                        return true;
                    }
                }
                return false;
            });
        }

        if (tag.equals("h1")) {
            System.out.println("h1");
        }
        // String class = element.

        return format;
    }

    private TextFormatEffect getFormat(Element element, List<CascadingStyleSheet> globalCss, TextFormatEffect effect) {
        return effect;
    }

    private void textNodeToResponse(TextNode node, List<TextElement> response, TextFormat format,
            TextFormatEffect formatEffect) {
        for (var c : node.toString().toCharArray()) {
            response.add(TextElementFactory.fromChar(this.storage, c, format, formatEffect));
        }
    }
}
