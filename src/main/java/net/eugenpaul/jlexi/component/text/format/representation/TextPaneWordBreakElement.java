package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextPaneWordBreakElement extends TextPaneElement {

    public TextPaneWordBreakElement(Glyph parent, TextElementV2 dataElement) {
        super(parent, dataElement);
    }

    @Override
    protected TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        switch (moving) {
        case NEXT, PREVIOUS:
            return null;
        case UP, DOWN, END_OF_LINE:
            if (parent instanceof TextRepresentationV2) {
                return ((TextRepresentationV2) parent).moveIn(MovePosition.PREVIOUS, fieldType, xOffset);
            }
            return super.moveIn(MovePosition.PREVIOUS, fieldType, xOffset);
        default:
            return super.moveIn(moving, fieldType, xOffset);
        }
    }

    @Override
    protected TextPositionV2 moveLast(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.END_OF_LINE, fieldType, xOffset);
    }

    @Override
    public TextPositionV2 getCursorElementAt(Vector2d pos) {
        //TODO: Add the possibility to set the cursor to separator.
        if (parent instanceof TextRepresentationV2) {
            return ((TextRepresentationV2) parent).moveIn(MovePosition.PREVIOUS, TextFieldType.UNKNOWN, pos.getX());
        }
        return null;
    }

}
