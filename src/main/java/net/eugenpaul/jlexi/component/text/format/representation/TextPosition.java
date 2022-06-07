package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextPosition {

    private TextElement textElement;

    public TextPosition(TextElement element) {
        this.textElement = element;
    }

    public boolean addBefore(TextElement element) {
        return this.textElement.addBefore(element);
    }

    public TextRemoveResponse removeElement() {
        return this.textElement.removeElement();
    }

    public TextRemoveResponse removeElementBefore() {
        return this.textElement.removeElementBefore();
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
