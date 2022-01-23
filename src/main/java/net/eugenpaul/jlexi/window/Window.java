package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.gui.AbstractPanel;

/**
 * Abstraction of a Window.
 */
public class Window {

    private Windowlmp windowlmp;

    public Window(Windowlmp windowlmp) {
        this.windowlmp = windowlmp;
    }

    public AbstractPanel createMainWindow(Glyph mGlyph) {
        return windowlmp.deviceCreateMainWindow(mGlyph);
    }
}
