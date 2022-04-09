package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.framing.MonoGlyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.RedrawData;
import net.eugenpaul.jlexi.exception.NotInitializedException;
import net.eugenpaul.jlexi.model.InterfaceModel;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.window.interfaces.WindowsKeyPressable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseClickable;
import net.eugenpaul.jlexi.window.interfaces.WindowsResizeable;
import net.eugenpaul.jlexi.window.propertychanges.UpdateTitle;

/**
 * Abstraction of a Window.
 */
public abstract class Window extends MonoGlyph
        implements WindowsKeyPressable, WindowsMouseClickable, WindowsResizeable, InterfaceModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Window.class);

    @Setter
    protected static WindowSystemFactory factory = null;
    protected final String name;
    protected final Windowlmp windowlmp;
    protected final ModelController controller;

    protected AbstractView view;
    @Getter
    private GuiComponent mainGlyph;
    @Getter
    protected KeyPressable focusOn;

    protected Window(String name, Size size, Windowlmp windowlmp, ModelController controller) {
        super(null, null);
        this.windowlmp = windowlmp;
        this.name = name;
        this.size = size;
        this.controller = controller;
        this.view = null;
        this.focusOn = null;
        this.mainGlyph = null;
    }

    protected <T extends Glyph & GuiComponent> void setMainGlyph(T glyph) {
        mainGlyph = glyph;
        component = glyph;
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
        if (getMainGlyph() != null) {
            getMainGlyph().onMouseClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMousePressed(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (getMainGlyph() != null) {
            getMainGlyph().onMousePressed(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseReleased(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (getMainGlyph() != null) {
            getMainGlyph().onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void resizeTo(String name, Size size) {
        if (getMainGlyph() != null) {
            getMainGlyph().resizeTo(size);
        }
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        LOGGER.trace("Window send notifyRedraw Data to window-impl");

        controller.propertyChange(new PropertyChangeEvent(//
                name, //
                ViewPropertyChangeType.DRAW_AREA.getTypeName(), //
                null, //
                new RedrawData(//
                        name, //
                        drawData, //
                        new Area(relativPosition.addNew(this.relativPosition), size)//
                ) //
        ));
    }

}
