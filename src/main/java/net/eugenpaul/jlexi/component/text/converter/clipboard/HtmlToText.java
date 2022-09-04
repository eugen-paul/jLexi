package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class HtmlToText {

    private ResourceManager storage;
    private Document doc;

    public HtmlToText(String clipboardHtml, ResourceManager storage) {
        this.doc = Jsoup.parse(clipboardHtml);
        System.out.println(clipboardHtml);
        System.out.println("---------------------------------");
        System.out.println(this.doc.outerHtml());
        System.out.println("---------------------------------");
        this.storage = storage;
    }

    public List<TextElement> convert() {

        for (var element : this.doc.childNodes()) {
            printChilds(element, 1);
        }

        return Collections.emptyList();
    }

    private void printChilds(Node node, int deep) {
        System.out.println(String.format("%" + deep + "s%s", " ", "nodename = " + node.nodeName() + " "));
        for (var child : node.childNodes()) {
            printChilds(child, deep + 1);
        }
    }
}
