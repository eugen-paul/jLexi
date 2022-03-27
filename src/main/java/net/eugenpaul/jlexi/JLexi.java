package net.eugenpaul.jlexi;

import java.io.File;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.font.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.impl.FormatStorageImpl;
import net.eugenpaul.jlexi.window.MainWindow;
import net.eugenpaul.jlexi.window.Window;
import net.eugenpaul.jlexi.window.impl.swing.SwingWindowFactory;

public class JLexi {

    public static void main(String[] args) {
        ModelController controller = new ModelController();

        FontStorage fonts = new FontStorageImpl(new FontGenerator());
        FormatStorage formats = new FormatStorageImpl();
        ResourceManager storage = new ResourceManager(fonts, formats);

        Window.setFactory(new SwingWindowFactory());

        MainWindow mainWindow = new MainWindow(//
                "MainWindow", //
                controller, //
                storage //
        );

        mainWindow.createWindow();

        mainWindow.loadFile((new File("src/main/resources/Progress.json")).toPath());

        mainWindow.setTitle("jLexi: Made by Eugen!");
    }
}
