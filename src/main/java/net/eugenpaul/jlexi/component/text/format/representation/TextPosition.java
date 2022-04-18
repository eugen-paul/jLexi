package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextPosition {

    private TextElement textElement;

    public TextPosition(TextElement element) {
        this.textElement = element;
    }

    public boolean addBefore(TextElement element) {
        return this.textElement.addBefore(element);
    }

    public TextPosition removeElement(List<TextElement> removedElements) {
        return this.textElement.removeElement(removedElements).getTextPosition();
    }

    public boolean removeElementBefore(List<TextElement> removedElements) {
        return this.textElement.removeElementBefore(removedElements);
    }

    public TextElement getTextElement() {
        return textElement;
    }

    public TextRepresentation getRepresentationChild(TextRepresentation representation) {
        var parent = textElement.getParent();
        Glyph previousParent = null;
        while (parent != null && parent != representation) {
            previousParent = parent;
            parent = parent.getParent();
        }

        if (previousParent instanceof TextRepresentation) {
            return (TextRepresentation) previousParent;
        }
        return null;
    }
}
