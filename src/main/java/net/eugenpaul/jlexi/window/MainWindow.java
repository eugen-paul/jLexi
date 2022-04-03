package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.eugenpaul.jlexi.component.framing.Border;
import net.eugenpaul.jlexi.component.framing.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.converter.json.JsonConverter;
import net.eugenpaul.jlexi.component.text.keyhandler.BoldFormatChangeListner;
import net.eugenpaul.jlexi.component.text.keyhandler.ItalicFormatChangeListner;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.design.TextButton;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class MainWindow extends ApplicationWindow {

    private static final String DEFAULT_TEXT_PANE_SUFFIX = "-TextPane";
    private static final Size DEFAULT_SIZE = new Size(800, 600);

    private ResourceManager storage;
    private String textPaneName;
    private GuiFactory factory;

    public MainWindow(String name, ModelController controller, ResourceManager storage, GuiFactory factory) {
        super(//
                name, //
                controller, //
                DEFAULT_SIZE //
        );

        this.storage = storage;
        this.factory = factory;
        this.textPaneName = name + DEFAULT_TEXT_PANE_SUFFIX;
    }

    @Override
    protected void setContent() {
        TextPane textPane = new TextPane(name, null, storage, controller);
        Border border = new Border(name, null, textPane);

        MenuBar menubar = new MenuBar(name, null, border, size, controller);
        initMenu(menubar);
        border.setParent(menubar);

        controller.addTextPane(textPaneName, textPane);

        controller.addWindow(this, name);
        controller.addModel(this);

        focusOn = textPane;
        mainGlyph = menubar;
    }

    private void initMenu(MenuBar menubar) {
        TextButton boldButton = factory.createTextButton(menubar, "B", storage);
        boldButton.setTextFormat(boldButton.getTextFormat().withBold(true));
        boldButton.setSize(new Size(20, 20));
        menubar.addMenuButton(boldButton);

        BoldFormatChangeListner boldListner = new BoldFormatChangeListner(boldButton, "textPaneCursor");
        this.controller.addView(boldListner);

        MouseEventAdapter mouseEventAdapter = new BoldActivate("textPaneCursor", boldButton, this.controller);
        boldButton.setMouseEventAdapter(mouseEventAdapter);

        TextButton italicButton = factory.createTextButton(menubar, "I", storage);
        boldButton.setTextFormat(boldButton.getTextFormat().withItalic(true));
        italicButton.setSize(new Size(20, 20));
        menubar.addMenuButton(italicButton);

        ItalicFormatChangeListner italicListner = new ItalicFormatChangeListner(italicButton, "textPaneCursor");
        this.controller.addView(italicListner);

        mouseEventAdapter = new ItalicActivate("textPaneCursor", italicButton, this.controller);
        italicButton.setMouseEventAdapter(mouseEventAdapter);
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
