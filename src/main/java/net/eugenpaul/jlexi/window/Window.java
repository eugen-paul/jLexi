package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Abstraction of a Window.
 */
public class Window {

    private Windowlmp windowlmp;

    public Window(Windowlmp windowlmp) {
        this.windowlmp = windowlmp;
    }

    public AbstractPanel createMainWindow(Size defaultSize) {
        return windowlmp.deviceCreateMainWindow(defaultSize);
    }
}
