package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.text.CursorV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;

/**
 * Interface for Text-Panel-Elements that can handle a key input
 */
public interface KeyHandlerableV2 {

    public CursorV2 getMouseCursor();

    public TextRepresentationV2 getTextRepresentation();
}
