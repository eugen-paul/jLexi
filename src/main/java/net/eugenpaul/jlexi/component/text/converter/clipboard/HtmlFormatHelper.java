package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.helger.css.decl.CascadingStyleSheet;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

public interface HtmlFormatHelper {

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
