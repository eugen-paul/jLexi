package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class ToHtmlConvertHelperImpl implements ToHtmlConvertHelper {

    @Override
    public String toHtml(List<TextElement> text) {
        return ClipboardHelper.addClipboardTags(
                "<html><body><h1>At some point, there will be reasonable code here.</h1></body></html>");
    }
}
