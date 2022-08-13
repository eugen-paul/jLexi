package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

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

    public boolean splitStructures(List<TextStructure> oldStructure, List<List<TextStructure>> newStructures) {
        return this.textElement.splitStructures(oldStructure, newStructures);
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
