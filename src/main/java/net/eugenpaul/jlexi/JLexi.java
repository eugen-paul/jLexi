package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.design.Panel;
import net.eugenpaul.jlexi.data.framing.Border;
import net.eugenpaul.jlexi.data.framing.MenuBar;
import net.eugenpaul.jlexi.data.framing.TextPane;
import net.eugenpaul.jlexi.data.stucture.CharGlyph;
import net.eugenpaul.jlexi.data.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.fontgenerator.FontPixelsGenerator;

public class JLexi {

    public static void main(String[] args) {
        DefaultController controller = new DefaultController();

        FontStorage fontStorage = new FontStorageImpl(new FontGenerator());
        Window windowAbstraction = new Window(new SwingWindowImpl(controller));

        // Panel ePanel = new Panel();
        // Border border = new Border(ePanel);

        TextPane textPane = new TextPane();
        Border border = new Border(textPane);
        addStringToTextPane(
                "A \"Hello, World!\" program is generally a computer program that outputs or displays the message \"Hello, World!\". "
                        + "This program is very simple to write in many programming languages, and is often used to illustrate a language's basic syntax.",
                textPane, fontStorage);

        Size defaultSize = new Size(800, 600);
        MenuBar menubar = new MenuBar(border, defaultSize, controller);
        controller.addModel(menubar);

        windowAbstraction.createMainWindow(menubar);

        controller.propertyChange(new PropertyChangeEvent(menubar, "UPDATE", null, defaultSize));
    }

    private static void addStringToTextPane(String text, TextPane textPane, FontStorage fontStorage) {
        for (int i = 0; i < text.length(); i++) {
            CharGlyph glyph = new CharGlyph(text.charAt(i), fontStorage);
            textPane.insert(glyph, i);
        }
    }
}
