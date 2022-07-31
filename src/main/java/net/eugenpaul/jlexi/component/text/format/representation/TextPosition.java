package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextPosition {

    @Getter
    private TextElement textElement;

    public TextPosition(TextElement element) {
        this.textElement = element;
    }

    public TextPosition getPreviousPosition() {
        return afterMove(MovePosition.PREVIOUS);
    }

    public TextPosition getNextPosition() {
        return afterMove(MovePosition.NEXT);
    }

    public boolean addBefore(TextElement element) {
        return this.textElement.addBefore(element);
    }

    public TextRemoveResponse removeElement() {
        return this.textElement.removeElement();
    }

    public TextPosition afterMove(MovePosition moving) {
        var parent = this.textElement.getParent();
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
