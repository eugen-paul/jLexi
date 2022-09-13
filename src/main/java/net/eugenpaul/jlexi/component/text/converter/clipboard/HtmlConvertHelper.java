package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CascadingStyleSheet;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

public interface HtmlConvertHelper {

    Map<String, TreeMap<Integer, CSSDeclaration>> stylesProirities(Node node, CascadingStyleSheet globalCss);

    TextFormat applyTagFormat( //
            String tagName, //
            Iterator<Map.Entry<String, String>> attributesiterator, //
            TextFormat currentFormat //
    );

    TextFormatEffect applyTagEffect(String tagName, TextFormatEffect currentEffect);

    TextFormat applyStyleAttr(String styleAttr, TextFormat format);

    TextFormat applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormat format);

    TextFormatEffect applyStyleAttr(String tagStyle, TextFormatEffect effect);

    TextFormatEffect applyStyleTagAttr(Element element, CascadingStyleSheet globalCss, TextFormatEffect effect);
}
