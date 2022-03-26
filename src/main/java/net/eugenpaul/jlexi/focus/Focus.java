package net.eugenpaul.jlexi.focus;

import net.eugenpaul.jlexi.component.interfaces.Focusable;

/**
 * Class for storing the current focus and forwarding user input (mouse and keyboard) to the correct editor component.
 */
public interface Focus {
    public void setFocus(Focusable component);
}
