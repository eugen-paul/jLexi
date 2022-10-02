package net.eugenpaul.jlexi.appl;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.eugenpaul.jlexi.appl.action.BoldActivate;
import net.eugenpaul.jlexi.appl.action.CopyActivate;
import net.eugenpaul.jlexi.appl.action.ItalicActivate;
import net.eugenpaul.jlexi.appl.action.PasteActivate;
import net.eugenpaul.jlexi.appl.action.RedoActivate;
import net.eugenpaul.jlexi.appl.action.UndoActivate;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.menubar.MenuBar;
import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.converter.json.JsonConverter;
import net.eugenpaul.jlexi.component.text.keyhandler.BoldFormatChangeListner;
import net.eugenpaul.jlexi.component.text.keyhandler.ItalicFormatChangeListner;
import net.eugenpaul.jlexi.config.Configurator;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.window.ApplicationWindow;

public class MainWindow extends ApplicationWindow {

    private static final String DEFAULT_TEXT_PANE_SUFFIX = "-TextPane";
    private static final Size DEFAULT_SIZE = new Size(800, 600);

    private ResourceManager storage;
    private String textPaneName;
    private GuiFactory guiFactory;
    private Configurator configurator;

    public MainWindow(String name, WindowController windowController, ResourceManager storage, GuiFactory guiFactory,
            Configurator configurator) {
        super(//
                name, //
                windowController, //
                DEFAULT_SIZE //
        );

        this.storage = storage;
        this.guiFactory = guiFactory;
        this.textPaneName = name + DEFAULT_TEXT_PANE_SUFFIX;
        this.configurator = configurator;
    }

    @Override
    protected GuiGlyph setContent() {
        var textPane = new TextPane(name, null, storage, controller);
        configurator.registerGui("textEditor", textPane);
        configurator.setAllsKeyBindings("textEditor");

        var scrollPane = guiFactory.createScrollpane(null, textPane);
        var border = guiFactory.createBorder(null, scrollPane);

        var menubar = guiFactory.createMenuBar(this, border, getSize());
        initMenu(menubar, textPane.getCursorName());
        menubar.setRelativPosition(Vector2d.zero());

        controller.addTextPane(textPaneName, textPane);

        controller.addWindow(this, name);
        controller.addModel(this);

        focusOn = textPane;
        return menubar;
    }

    private void initMenu(MenuBar menubar, String cursorName) {
        addBoldButton(menubar, cursorName);
        addItalicButton(menubar, cursorName);

        addUndoButton(menubar, cursorName);
        addRedoButton(menubar, cursorName);

        addCopyButton(menubar, cursorName);
        addPasteButton(menubar, cursorName);
    }

    private void addBoldButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, "B", storage);
        button.setTextFormat(button.getTextFormat().withBold(true));
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        BoldFormatChangeListner listner = new BoldFormatChangeListner(button, cursorName);
        this.controller.addViewChangeListner(listner);

        MouseEventAdapter mouseEventAdapter = new BoldActivate(cursorName, button, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
    }

    private void addItalicButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, "I", storage);
        button.setTextFormat(button.getTextFormat().withItalic(true));
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        ItalicFormatChangeListner listner = new ItalicFormatChangeListner(button, cursorName);
        this.controller.addViewChangeListner(listner);

        MouseEventAdapter mouseEventAdapter = new ItalicActivate(cursorName, button, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
    }

    private void addUndoButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, "<", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        MouseEventAdapter mouseEventAdapter = new UndoActivate(cursorName, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
    }

    private void addRedoButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, ">", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        MouseEventAdapter mouseEventAdapter = new RedoActivate(cursorName, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
    }

    private void addCopyButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, "C", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        MouseEventAdapter mouseEventAdapter = new CopyActivate(cursorName, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
    }

    private void addPasteButton(MenuBar menubar, String cursorName) {
        TextButton button = guiFactory.createTextButton(menubar, "P", storage);
        button.setTextFormat(button.getTextFormat());
        button.setSize(new Size(20, 20));
        menubar.addMenuButton(button);

        MouseEventAdapter mouseEventAdapter = new PasteActivate(cursorName, this.controller);
        button.setMouseEventAdapter(mouseEventAdapter);
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
            controller.setText(textPaneName, textSections);
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
