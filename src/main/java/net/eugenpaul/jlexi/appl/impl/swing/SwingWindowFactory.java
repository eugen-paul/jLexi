package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.window.WindowSystemFactory;
import net.eugenpaul.jlexi.window.Windowlmp;

@AllArgsConstructor
public class SwingWindowFactory implements WindowSystemFactory {

    private ExecutorService pool;

    @Override
    public Function<WindowController, Windowlmp> createApplicationWindow() {
        return controller -> new SwingWindowImpl(controller, pool);
    }

}
