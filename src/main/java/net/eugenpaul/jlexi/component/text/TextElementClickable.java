package net.eugenpaul.jlexi.component.text;

import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Object that can receive mouseclick notification from gui.
 */
public interface TextElementClickable {
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button);
}
