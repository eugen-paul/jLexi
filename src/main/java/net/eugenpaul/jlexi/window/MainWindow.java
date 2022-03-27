package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.eugenpaul.jlexi.component.framing.Border;
import net.eugenpaul.jlexi.component.framing.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.converter.json.JsonConverter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class MainWindow extends ApplicationWindow {

    private static final String DEFAULT_TEXT_PANE_SUFFIX = "-TextPane";
    private static final Size DEFAULT_SIZE = new Size(800, 600);

    private ResourceManager storage;
    private String textPaneName;

    public MainWindow(String name, ModelController controller, ResourceManager storage) {
        super(//
                name, //
                controller, //
                DEFAULT_SIZE //
        );

        this.storage = storage;
        this.textPaneName = name + DEFAULT_TEXT_PANE_SUFFIX;
    }

    @Override
    protected void setContent() {
        TextPane textPane = new TextPane(name, null, storage, controller);
        Border border = new Border(name, null, textPane);

        MenuBar menubar = new MenuBar(name, null, border, size, controller);
        border.setParent(menubar);

        controller.addTextPane(textPaneName, textPane);

        controller.addWindow(this, name);
        controller.addModel(this);

        focusOn = textPane;
        mainGlyph = menubar;
    }

    /**
     * load text file to textpane
     * 
     * @param path
     * @return
     */
    public boolean loadFile(Path path) {
        JsonConverter converter = new JsonConverter(storage);
        try {
            var fileData = Files.readString(path);
            var textElements = converter.read(fileData);
            controller.setText(textPaneName, textElements);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        view.modelPropertyChange(//
                new PropertyChangeEvent(name, //
                        ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), //
                        null, //
                        size//
                )//
        );

        return true;
    }

}
