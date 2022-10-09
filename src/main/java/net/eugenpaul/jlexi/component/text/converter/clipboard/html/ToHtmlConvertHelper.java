package net.eugenpaul.jlexi.component.text.converter.clipboard.html;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface ToHtmlConvertHelper {
    String toHtml(List<TextElement> text);
}
