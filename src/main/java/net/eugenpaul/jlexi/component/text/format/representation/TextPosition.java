package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.CursorMoving;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextAddResponse;
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

    public TextAddResponse addBefore(TextElement element) {
        return this.textElement.addBefore(element);
    }

    public boolean replaceStructure(//
            TextStructure owner, //
            List<TextStructure> oldStructure, //
            List<TextStructure> newStructure //
    ) {
        return this.textElement.replaceStructure(owner, oldStructure, newStructure);
    }

    public TextRemoveResponse removeElement() {
        return this.textElement.removeElement();
    }

    public TextPosition afterMove(MovePosition moving) {
        var parent = this.textElement.getParent();
        while (parent != null) {
            if (parent instanceof CursorMoving) {
                CursorMoving representation = (CursorMoving) parent;
                return representation.move(this, moving);
            }
            parent = parent.getParent();
        }
        return null;
    }
}
