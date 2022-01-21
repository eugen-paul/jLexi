package net.eugenpaul.jlexi.data.stucture;

import net.eugenpaul.jlexi.data.Glyph;

/**
 * Interface for all elements (CharGlyph, TableGlyph, ImageGlyph) that could be added to Text-Gui-Components (TaxtPane,
 * Label, ...)
 */
public abstract class TextPaneElement extends Glyph {

    protected TextPaneElement(Glyph parent) {
        super(parent);
    }

    /**
     * The function checks if the element can hold the mouse cursor. For example, a table can contain the cursor, so a
     * keyboard event can be handled by the table. An image element, on the other hand, cannot contain a cursor. In this
     * case, the parent element of the image should process the keyboard event.
     * 
     * @return true if the element can hold the mouse cursor.
     */
    public abstract boolean isCursorHoldable();
}
