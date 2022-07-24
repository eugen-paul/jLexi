package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;

public class TextPositionV2 {

    private TextElement textElement;

    public TextPositionV2(TextElement element) {
        this.textElement = element;
    }

    public TextPositionV2 getPreviousPosition() {
        //TODO
        return null;
    }
    
    public TextPositionV2 getNextPosition() {
        //TODO
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

    public TextPositionV2 moveTo(MovePosition moving, TextRepresentation parentRepresentation) {
        var parent = textElement.getParent();
        Glyph previousParent = null;
        while (parent != null && parent != parentRepresentation) {
            previousParent = parent;
            parent = parent.getParent();
        }

        if (previousParent instanceof TextRepresentation) {
            TextRepresentation representation = (TextRepresentation) previousParent;
            switch (moving) {
            case UP:
                return representation.getUp(this);
            case DOWN:
                return representation.getDown(this);
            default:
                throw new NotYetImplementedException("Move TextPosition to " + moving + " is not implemented.");
            }
        }

        return null;
    }
}
