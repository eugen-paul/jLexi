package net.eugenpaul.jlexi.component.interfaces;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface TextUpdateable {
    public void setText(String text);

    public void setText(List<TextElement> text);
}
