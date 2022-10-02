package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.function.Function;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.window.WindowSystemFactory;
import net.eugenpaul.jlexi.window.Windowlmp;

public class SwingWindowFactory implements WindowSystemFactory {

    @Override
    public Function<ModelController, Windowlmp> createApplicationWindow() {
        return SwingWindowImpl::new;
    }

}
