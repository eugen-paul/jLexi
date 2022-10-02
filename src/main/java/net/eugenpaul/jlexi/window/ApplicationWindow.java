package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.utils.Size;

public abstract class ApplicationWindow extends Window {

    protected ApplicationWindow(String name, WindowController windowController, Size size) {
        super(//
                name, //
                size, //
                factory.createApplicationWindow().apply(windowController), //
                windowController //
        );
    }

}
