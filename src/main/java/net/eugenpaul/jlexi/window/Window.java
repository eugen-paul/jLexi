package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import lombok.Setter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.exception.NotInitializedException;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.window.propertychanges.UpdateTitle;

/**
 * Abstraction of a Window.
 */
public abstract class Window {

    @Setter
    protected static WindowSystemFactory factory = null;
    protected final String name;
    protected final Windowlmp windowlmp;
    protected Size size;
    protected final ModelController controller;

    protected AbstractView view;

    protected Window(String name, Size size, Windowlmp windowlmp, ModelController controller) {
        this.windowlmp = windowlmp;
        this.name = name;
        this.size = size;
        this.controller = controller;
        this.view = null;
    }

    public AbstractView createWindow() {

        if (factory == null) {
            throw new NotInitializedException("WindowSystemFactory ist not initialized/set.");
        }

        if (view != null) {
            return view;
        }

        setContent();
        view = windowlmp.deviceCreateMainWindow(size, name);

        view.modelPropertyChange(//
                new PropertyChangeEvent(name, //
                        ViewPropertyChangeType.TRIGGER_FULL_DRAW.getTypeName(), //
                        null, //
                        size//
                )//
        );

        return view;
    }

    protected abstract void setContent();

    public void setTitle(String title) {
        view.modelPropertyChange(new PropertyChangeEvent(name, //
                ViewPropertyChangeType.SET_TITLE.getTypeName(), //
                null, //
                new UpdateTitle(name, title)));
    }

}
