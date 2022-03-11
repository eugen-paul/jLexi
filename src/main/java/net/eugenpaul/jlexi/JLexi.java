package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.component.framing.Border;
import net.eugenpaul.jlexi.component.framing.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.utils.Size;

public class JLexi {

    private static final String MAIN_WINDOW = "MainWindow";

    public static void main(String[] args) {
        ModelController controller = new ModelController();

        FontStorage fonts = new FontStorageImpl(new FontGenerator());
        FormatStorage formats = new FormatStorageImpl();
        ResourceManager storage = new ResourceManager(fonts, formats);

        Window windowAbstraction = new Window(new SwingWindowImpl(controller));

        TextPane textPane = new TextPane(null, storage, controller);
        Border border = new Border(null, textPane);

        controller.addTextPane("TextPane", textPane);

        controller.setText("TextPane",
                "A \"Hello, World!\" program is generally a computer program that outputs or displays the message \"Hello, World!\". "
                        + "This program is very simple to write in many programming languages, and is often used to illustrate a language's basic syntax.\n"
                        + "Text can be: {BI:bold and italic}, {B :bold} or { I:italic}.");

        Size defaultSize = new Size(800, 600);
        MenuBar menubar = new MenuBar(MAIN_WINDOW, null, border, defaultSize, controller);
        border.setParent(menubar);
        controller.addModel(menubar);

        windowAbstraction.createMainWindow(defaultSize);

        controller.addGlyph(menubar, MAIN_WINDOW);

        controller.propertyChange(new PropertyChangeEvent(MAIN_WINDOW, //
                ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), //
                null, //
                defaultSize));
    }
}
