package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Node;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CascadingStyleSheet;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

public interface HtmlConvertHelper {

    Map<String, TreeMap<Integer, CSSDeclaration>> stylesProperties(Node node, CascadingStyleSheet globalCss);

    TextFormat applyFormatAttributes(TextFormat format, Map<String, TreeMap<Integer, CSSDeclaration>> properties);

    TextFormatEffect applyEffectAttributes(TextFormatEffect effect,
            Map<String, TreeMap<Integer, CSSDeclaration>> properties);
}
