package net.eugenpaul.jlexi.window.interfaces;

import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Object that can receive mousedrug notification from gui.
 */
public interface WindowsMouseDrugable {
    public void onMouseDrug(String name, Integer mouseX, Integer mouseY, MouseButton button);
}
