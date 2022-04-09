package net.eugenpaul.jlexi.window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.Size;

public abstract class ApplicationWindow extends Window {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWindow.class);

    protected ApplicationWindow(String name, ModelController controller, Size size) {
        super(//
                name, //
                size, //
                factory.createApplicationWindow().apply(controller), //
                controller //
        );

        this.focusOn = null;
    }

}
