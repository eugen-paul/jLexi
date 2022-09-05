package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
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

        for (var element : this.doc.childNodes()) {
            printChilds(element, 1);
        }

        return Collections.emptyList();
    }

    private void printChilds(Node node, int deep) {
        System.out.println(String.format("%" + deep + "s%s", " ", "nodename = " + node.nodeName() + " "));
        for (Node child : node.childNodes()) {
            printChilds(child, deep + 1);
        }
    }
}
