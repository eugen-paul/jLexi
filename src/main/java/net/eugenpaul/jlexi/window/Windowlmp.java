package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.gui.AbstractPanel;

public abstract class Windowlmp {

    protected ModelController controller;

    protected Windowlmp(ModelController controller) {
        this.controller = controller;
    }

    public abstract AbstractPanel deviceCreateMainWindow(Glyph mGlyph);

}
