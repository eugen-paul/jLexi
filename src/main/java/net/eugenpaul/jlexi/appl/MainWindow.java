package net.eugenpaul.jlexi.appl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.eugenpaul.jlexi.appl.action.ButtonAction;
import net.eugenpaul.jlexi.appl.action.ButtonBoldAction;
import net.eugenpaul.jlexi.appl.action.ButtonItalicAction;
import net.eugenpaul.jlexi.appl.subscriber.GlobalSubscribeTypes;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.menubar.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.converter.json.JsonConverter;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.window.ApplicationWindow;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;

public class MainWindow extends ApplicationWindow {

    private static final Size DEFAULT_SIZE = new Size(800, 600);

    private ResourceManager storage;
    private GuiFactory guiFactory;
    private EventManager eventManager;

    private TextPane textPane;

    public MainWindow(String name, WindowController controller, ResourceManager storage, GuiFactory guiFactory,
            EventManager eventManager) {
        super(//
                name, //
                controller, //
                DEFAULT_SIZE //
        );

        this.storage = storage;
        this.guiFactory = guiFactory;
        this.eventManager = eventManager;
    }

    @Override
    protected GuiGlyph setContent() {
        textPane = new TextPane(name, "textEditor", null, storage, eventManager);
        eventManager.fireEvent(this, GlobalSubscribeTypes.REGISTER_GUI_ELEMENT, textPane);

        var scrollPane = guiFactory.createScrollpane(null, textPane);
        var border = guiFactory.createBorder(null, scrollPane);

        var menubar = guiFactory.createMenuBar(this, border, getSize());
        initMenu(menubar, textPane.getCursorName(), textPane);
        menubar.setRelativPosition(Vector2d.zero());

        controller.addWindow(this, name);
        controller.addModel(this);

        focusOn = textPane;
        return menubar;
    }

    private void initMenu(MenuBar menubar, String cursorName, GuiGlyph textPane) {
        addBoldButton(menubar, cursorName, textPane.getDefaultKeyBinding("bold"));
        addItalicButton(menubar, cursorName, textPane.getDefaultKeyBinding("italic"));

        addUndoButton(menubar, textPane.getDefaultKeyBinding("undo"));
        addRedoButton(menubar, textPane.getDefaultKeyBinding("redo"));

        addCopyButton(menubar, textPane.getDefaultKeyBinding("copy"));
        addPasteButton(menubar, textPane.getDefaultKeyBinding("paste"));
    }

    private void addBoldButton(MenuBar menubar, String cursorName, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, "B", storage);
        button.setTextFormat(button.getTextFormat().withBold(true));
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonBoldAction(button, action, cursorName);
        eventManager.addSubscriber(buttonAction);
        button.setMouseEventAdapter(buttonAction);
    }

    private void addItalicButton(MenuBar menubar, String cursorName, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, "I", storage);
        button.setTextFormat(button.getTextFormat().withItalic(true));
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonItalicAction(button, action, cursorName);
        eventManager.addSubscriber(buttonAction);
        button.setMouseEventAdapter(buttonAction);
    }

    private void addUndoButton(MenuBar menubar, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, "<", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonAction(action);
        button.setMouseEventAdapter(buttonAction);
    }

    private void addRedoButton(MenuBar menubar, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, ">", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonAction(action);
        button.setMouseEventAdapter(buttonAction);
    }

    private void addCopyButton(MenuBar menubar, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, "C", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonAction(action);
        button.setMouseEventAdapter(buttonAction);
    }

    private void addPasteButton(MenuBar menubar, KeyBindingAction action) {
        TextButton button = guiFactory.createTextButton(menubar, "P", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        var buttonAction = new ButtonAction(action);
        button.setMouseEventAdapter(buttonAction);
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
            var textSections = converter.read(fileData);
            textPane.setText(textSections);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        view.modelPropertyChange(//
                new PropertyChangeEvent(name, //
                        ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), //
                        null, //
                        getSize()//
                )//
        );

        return true;
    }

}
