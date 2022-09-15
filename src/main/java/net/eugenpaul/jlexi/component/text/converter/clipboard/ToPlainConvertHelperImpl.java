package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class ToPlainConvertHelperImpl implements ToPlainConvertHelper {

    @Override
    public String toPlain(List<TextElement> text) {
        StringBuilder textBuilder = new StringBuilder(text.size());
        for (var element : text) {
            textBuilder.append(element.toString());
        }

        return textBuilder.toString();
    }
}
