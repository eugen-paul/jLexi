package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.framing.Border;
import net.eugenpaul.jlexi.data.framing.MenuBar;
import net.eugenpaul.jlexi.data.framing.TextPane;
import net.eugenpaul.jlexi.data.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;

public class JLexi {

    public static void main(String[] args) {
        DefaultController controller = new DefaultController();

        FontStorage fontStorage = new FontStorageImpl(new FontGenerator());
        Window windowAbstraction = new Window(new SwingWindowImpl(controller));

        // Panel ePanel = new Panel();
        // Border border = new Border(ePanel);

        TextPane textPane = new TextPane(null, fontStorage);
        Border border = new Border(null, textPane);
        textPane.setText(
                "A \"Hello, World!\" program is generally a computer program that outputs or displays the message \"Hello, World!\". "
                        + "This program is very simple to write in many programming languages, and is often used to illustrate a language's basic syntax.");

        Size defaultSize = new Size(800, 600);
        MenuBar menubar = new MenuBar(null, border, defaultSize, controller);
        controller.addModel(menubar);

        windowAbstraction.createMainWindow(menubar);

        controller.propertyChange(new PropertyChangeEvent(menubar, "UPDATE", null, defaultSize));
    }
}
