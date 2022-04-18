package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;

/**
 * Interface for Text-Panel-Elements that can handle a key input
 */
public interface KeyHandlerable {

    public Cursor getMouseCursor();

    public TextRepresentation getTextRepresentation();
}
