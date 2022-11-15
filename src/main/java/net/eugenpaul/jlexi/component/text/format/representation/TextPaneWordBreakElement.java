package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;

public class TextPaneWordBreakElement extends TextPaneElement {

    public TextPaneWordBreakElement(Glyph parent, TextElementV2 dataElement) {
        super(parent, dataElement);
    }

    @Override
    protected TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        switch (moving) {
        case NEXT, PREVIOUS:
            return null;
        default:
            return super.moveIn(moving, fieldType, xOffset);
        }
    }
}
