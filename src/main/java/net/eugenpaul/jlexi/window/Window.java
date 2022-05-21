package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.MonoGlyph;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.exception.NotInitializedException;
import net.eugenpaul.jlexi.model.InterfaceModel;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.window.interfaces.WindowsKeyPressable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseClickable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseWheel;
import net.eugenpaul.jlexi.window.interfaces.WindowsResizeable;
import net.eugenpaul.jlexi.window.propertychanges.UpdateTitle;

/**
 * Abstraction of a Window.
 */
public abstract class Window extends MonoGlyph
        implements WindowsKeyPressable, WindowsMouseClickable, WindowsResizeable, WindowsMouseWheel, InterfaceModel {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class);

    @Setter
    protected static WindowSystemFactory factory = null;
    protected final String name;
    protected final Windowlmp windowlmp;
    protected final ModelController controller;

    protected AbstractView view;
    private GuiEvents mainGlyph;
    protected KeyPressable focusOn;

    protected Window(String name, Size size, Windowlmp windowlmp, ModelController controller) {
        super(null, null);
        this.windowlmp = windowlmp;
        this.name = name;
        setSize(size);
        this.controller = controller;
        this.view = null;
        this.focusOn = null;
        this.mainGlyph = null;
    }

    protected <T extends Glyph & GuiEvents> void setMainGlyph(T glyph) {
        mainGlyph = glyph;
        component = glyph;
    }

    public Glyph getGlyph() {
        return component;
    }

    public AbstractView createWindow() {

        if (factory == null) {
            throw new NotInitializedException("WindowSystemFactory ist not initialized/set.");
        }

        if (view != null) {
            return view;
        }

        setContent();
        view = windowlmp.deviceCreateMainWindow(getSize(), name);

        redraw();

        return view;
    }

    @Override
    public void redraw() {
        getDrawable();

        view.modelPropertyChange(//
                new PropertyChangeEvent(name, //
                        ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), //
                        null, //
                        getSize()//
                )//
        );
    }

    protected abstract void setContent();

    public void setTitle(String title) {
        view.modelPropertyChange(new PropertyChangeEvent(name, //
                ViewPropertyChangeType.SET_TITLE.getTypeName(), //
                null, //
                new UpdateTitle(name, title)));
    }

    @Override
    public void onKeyTyped(String name, Character key) {
        if (focusOn != null) {
            focusOn.onKeyTyped(key);
        }
    }

    @Override
    public void onKeyPressed(String name, KeyCode keyCode) {
        if (focusOn != null) {
            focusOn.onKeyPressed(keyCode);
        }
    }

    @Override
    public void onKeyReleased(String name, KeyCode keyCode) {
        if (focusOn != null) {
            focusOn.onKeyReleased(keyCode);
        }
    }

    @Override
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMouseClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMousePressed(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMousePressed(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseReleased(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseWheelMooved(String name, Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (mainGlyph != null) {
            mainGlyph.onMouseWhellMoved(mouseX, mouseY, direction);
        }
    }

    @Override
    public void resizeTo(String name, Size size) {
        if (mainGlyph != null) {
            mainGlyph.resizeTo(size);
        }
    }

}
