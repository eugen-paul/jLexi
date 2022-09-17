package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface ToPlainConvertHelper {
    String toPlain(List<TextElement> text);
}
