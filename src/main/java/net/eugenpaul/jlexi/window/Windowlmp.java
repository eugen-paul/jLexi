package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.Size;

public abstract class Windowlmp {

    protected ModelController controller;

    protected Windowlmp(ModelController controller) {
        this.controller = controller;
    }

    public abstract AbstractView deviceCreateMainWindow(Size defaultSize, String name);

}
