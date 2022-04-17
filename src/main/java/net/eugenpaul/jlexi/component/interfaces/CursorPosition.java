package net.eugenpaul.jlexi.component.interfaces;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface CursorPosition {
    boolean addBefore(TextElement element);

    CursorPosition removeElement(List<TextElement> removedElements);

    boolean removeElementBefore(List<TextElement> removedElements);

    void notifyChange();

    TextElement getTextElement();
}
