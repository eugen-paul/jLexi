package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.stucture.TextPaneElement;
import net.eugenpaul.jlexi.utils.NodeList.NodeListElement;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface TextElementClickable {
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button);
}
