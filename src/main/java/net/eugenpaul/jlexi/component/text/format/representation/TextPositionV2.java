package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.CursorMovingV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextAddResponse;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;

public class TextPositionV2 {

    @Getter
    private TextElementV2 textElement;

    public TextPositionV2(TextElementV2 element) {
        this.textElement = element;
    }

    public TextPositionV2 getPreviousPosition() {
        return afterMove(MovePosition.PREVIOUS);
    }

    public TextPositionV2 getNextPosition() {
        return afterMove(MovePosition.NEXT);
    }

    public TextAddResponse addBefore(TextElementV2 element) {
        // TODO
        // return this.textElement.addBefore(element);
        return TextAddResponse.EMPTY;
    }

    public boolean replaceStructure(//
            TextStructureV2 owner, //
            List<TextStructureV2> oldStructure, //
            List<TextStructureV2> newStructure //
    ) {
        return this.textElement.replaceStructure(owner, oldStructure, newStructure);
    }

    public TextRemoveResponse removeElement() {
        // TODO
        // return this.textElement.removeElement();
        return TextRemoveResponse.EMPTY;
    }

    public TextPositionV2 afterMove(MovePosition moving) {
        var textReplList = this.textElement.getRepresentation();

        if (textReplList == null || textReplList.isEmpty()) {
            return null;
        }

        var parent = textReplList.get(0);
        if (parent instanceof CursorMovingV2) {
            var representation = (CursorMovingV2) parent;
            return representation.move(this, moving);
        }

        return null;
    }
}
