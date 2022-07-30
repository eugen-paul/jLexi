package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextPositionV2 {

    private TextElement textElement;

    public TextPositionV2(TextElement element) {
        this.textElement = element;
    }

    public TextPositionV2 getPreviousPosition() {
        // TODO
        return null;
    }

    public TextPositionV2 getNextPosition() {
        // TODO
        return null;
    }

    public boolean addBefore(TextElement element) {
        return this.textElement.addBefore(element);
    }

    public TextRemoveResponse removeElement() {
        return this.textElement.removeElement();
    }

    public TextElement getTextElement() {
        return textElement;
    }

    public TextPositionV2 move(MovePosition moving) {
        var parent = textElement.getParent();
        while (parent != null) {
            if (parent instanceof TextRepresentation) {
                TextRepresentation representation = (TextRepresentation) parent;
                return representation.move(this, moving);
            }
            parent = parent.getParent();
        }
        return null;
    }
}
