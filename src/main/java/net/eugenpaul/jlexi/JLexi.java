package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.component.framing.Border;
import net.eugenpaul.jlexi.component.framing.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.utils.Size;

public class JLexi {

    public static void main(String[] args) {
        ModelController controller = new ModelController();

        FontStorage fontStorage = new FontStorageImpl(new FontGenerator());
        Window windowAbstraction = new Window(new SwingWindowImpl(controller));

        TextPane textPane = new TextPane(null, fontStorage, controller);
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
