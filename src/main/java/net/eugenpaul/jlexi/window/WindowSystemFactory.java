package net.eugenpaul.jlexi.window;

import java.util.function.Function;

import net.eugenpaul.jlexi.controller.ModelController;

public interface WindowSystemFactory {
    Function<ModelController, Windowlmp> createApplicationWindow();
}
