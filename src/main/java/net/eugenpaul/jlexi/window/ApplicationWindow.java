package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.Size;

public abstract class ApplicationWindow extends Window {

    protected ApplicationWindow(String name, ModelController controller, Size size) {
        super(//
                name, //
                size, //
                factory.createApplicationWindow().apply(controller), //
                controller //
        );
    }

}
