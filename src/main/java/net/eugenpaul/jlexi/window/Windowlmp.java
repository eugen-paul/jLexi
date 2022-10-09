package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.utils.Size;

public abstract class Windowlmp {

    protected WindowController controller;

    protected Windowlmp(WindowController controller) {
        this.controller = controller;
    }

    public abstract AbstractView deviceCreateMainWindow(Size defaultSize, String name, GuiGlyph mainGlyph);

}
