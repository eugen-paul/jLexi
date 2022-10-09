package net.eugenpaul.jlexi.window;

import java.util.function.Function;

import net.eugenpaul.jlexi.controller.WindowController;

public interface WindowSystemFactory {
    Function<WindowController, Windowlmp> createApplicationWindow();
}
