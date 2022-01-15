package net.eugenpaul.jlexi.data.window;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.gui.AbstractPanel;

public abstract class Windowlmp {

    protected DefaultController controller;

    protected Windowlmp(DefaultController controller) {
        this.controller = controller;
    }

    public abstract AbstractPanel deviceCreateMainWindow(Glyph mGlyph);

}
