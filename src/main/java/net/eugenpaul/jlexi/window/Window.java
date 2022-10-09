package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.MonoGlyph;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.exception.NotInitializedException;
import net.eugenpaul.jlexi.model.InterfaceModel;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.window.interfaces.WindowsKeyPressable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseClickable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseDrugable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseWheel;
import net.eugenpaul.jlexi.window.interfaces.WindowsResizeable;
import net.eugenpaul.jlexi.window.propertychanges.UpdateTitle;

/**
 * Abstraction of a Window.
 */
public abstract class Window extends MonoGlyph implements WindowsKeyPressable, WindowsMouseClickable, WindowsResizeable,
        WindowsMouseWheel, WindowsMouseDrugable, InterfaceModel {

    private static final String DEFAULT_TITLE = "Window";

    @Setter
    protected static WindowSystemFactory factory = null;

    @Getter
    protected final String name;
    protected final Windowlmp windowlmp;

    protected AbstractView view;
    protected String title;
    private GuiGlyph mainGlyph;
    protected KeyPressable focusOn;

    private PropertyChangeSupport propertyChangeSupport;

    private MouseDraggable mousePressedOn;

    protected Window(String name, Size size, Windowlmp windowlmp) {
        super(null, null);
        this.windowlmp = windowlmp;
        this.name = name;
        setSize(size);
        this.view = null;
        this.focusOn = null;
        this.mainGlyph = null;
        this.title = DEFAULT_TITLE;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
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

        firePropertyChange(ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), null, name);
    }

    protected abstract GuiGlyph setContent();

    public void setTitle(String title) {
        this.title = title;

        firePropertyChange(ViewPropertyChangeType.SET_MAIN_TITLE.getTypeName(), null, new UpdateTitle(name, title));
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
    public void onMouseDrug(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mousePressedOn != null) {
            mousePressedOn.onMouseDragged(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMousePressed(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mousePressedOn = mainGlyph.onMousePressed(mouseX, mouseY, button);
        } else {
            mousePressedOn = null;
        }
    }

    @Override
    public void onMouseReleased(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMouseReleased(mouseX, mouseY, button);
        }
        mousePressedOn = null;
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

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
