package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.helger.css.decl.CascadingStyleSheet;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

public interface HtmlFormatHelper {

    TextFormat applyTagFormat( //
            String tagName, //
            Iterator<Map.Entry<String, String>> attributesiterator, //
            TextFormat currentFormat //
    );

    public TextFormatEffect applyTagEffect(String tagName, TextFormatEffect currentEffect);

    public TextFormat applyStyleAttr(String styleAttr, TextFormat format);

    public TextFormat applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormat format);

    public TextFormatEffect applyStyleAttr(Node node, String tagStyle, TextFormatEffect effect);

    public TextFormatEffect applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormatEffect effect);

}
