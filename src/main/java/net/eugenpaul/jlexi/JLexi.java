package net.eugenpaul.jlexi;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.appl.MainWindow;
import net.eugenpaul.jlexi.appl.impl.swing.SwingWindowFactory;
import net.eugenpaul.jlexi.config.Configurator;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.design.dark.DarkFactory;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.font.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.impl.FormatStorageImpl;
import net.eugenpaul.jlexi.window.Window;

@Slf4j
public class JLexi {

    public static void main(String[] args) throws IOException {
        ModelController controller = new ModelController();
        WindowController windowController = new WindowController();

        FontStorage fonts = new FontStorageImpl(new FontGenerator());
        FormatStorage formats = new FormatStorageImpl();
        ResourceManager storage = new ResourceManager(fonts, formats);

        Window.setFactory(new SwingWindowFactory());

        Configurator config = new Configurator("src/main/resources/configuratioin.json");
        try {
            config.init();
        } catch (IOException e) {
            LOGGER.error("Load configuration error", e);
            throw e;
        }

        MainWindow mainWindow = new MainWindow(//
                "MainWindow", //
                windowController, //
                storage, //
                new DarkFactory(), //
                config //
        );

        mainWindow.createWindow();

        mainWindow.loadFile((new File("src/main/resources/testexamples/json/Progress.json")).toPath());

        mainWindow.redraw();

        mainWindow.setTitle("jLexi: Made by Eugen!");
    }
}
