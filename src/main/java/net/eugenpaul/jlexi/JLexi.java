package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.eugenpaul.jlexi.component.framing.Border;
import net.eugenpaul.jlexi.component.framing.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.converter.json.JsonConverter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.font.FontStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator.FontGenerator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.impl.FormatStorageImpl;
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

        JsonConverter converter = new JsonConverter(storage);
        try {
            var text = converter.read(//
                    Files.readString(//
                            (new File("src/main/resources/Progress.json")).toPath()//
                    )//
            );
            controller.setText("TextPane", text);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

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
