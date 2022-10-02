package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.MonoGlyph;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.controller.WindowController;
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

    @Setter
    protected static WindowSystemFactory factory = null;
    protected final String name;
    protected final Windowlmp windowlmp;
    protected final WindowController controller;

    protected AbstractView view;
    private GuiGlyph mainGlyph;
    protected KeyPressable focusOn;

    protected Window(String name, Size size, Windowlmp windowlmp, WindowController windowController) {
        super(null, null);
        this.windowlmp = windowlmp;
        this.name = name;
        setSize(size);
        this.controller = windowController;
        this.view = null;
        this.focusOn = null;
        this.mainGlyph = null;
    }

    public Glyph getGlyph() {
        return component;
    }

    public AbstractView createWindow() {

        if (factory == null) {
            throw new NotInitializedException("WindowSystemFactory ist not initialized/set.");
        }

        if (this.view != null) {
            return this.view;
        }

        this.mainGlyph = setContent();
        this.component = mainGlyph;
        this.view = windowlmp.deviceCreateMainWindow(getSize(), name, mainGlyph);

        redraw();

        return this.view;
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

    protected abstract GuiGlyph setContent();

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
            var mousePressedOn = mainGlyph.onMousePressed(mouseX, mouseY, button);
            controller.mousePressedOn(mousePressedOn);
        } else {
            controller.mousePressedOn(null);
        }
    }

    @Override
    public void onMouseReleased(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMouseReleased(mouseX, mouseY, button);
        }
        controller.mousePressedOn(null);
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
