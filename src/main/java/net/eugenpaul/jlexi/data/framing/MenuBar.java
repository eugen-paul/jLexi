package net.eugenpaul.jlexi.data.framing;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.design.GuiComponent;
import net.eugenpaul.jlexi.model.InterfaceModel;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph implements GuiComponent, InterfaceModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBar.class);

    private static final int MENUBAR_HIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    private static final int[] EMPTY_MENUBAR = new int[0];
    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    private int[] menuBackground;
    private AbstractController controller;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBar(Glyph parent, Glyph component, Size size, AbstractController controller) {
        super(parent, component);
        component.setParent(this);
        setSize(size);
        this.controller = controller;

        computePixels();
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.resizeTo(getSize().getWidth(), Math.max(0, getSize().getHight() - MENUBAR_HIGHT));
        }

        this.menuBackground = generateMenuBackground(//
                getSize().getWidth(), //
                Math.min(getSize().getHight(), MENUBAR_HIGHT)//
        );
    }

    @Override
    public Drawable getPixels() {
        if (getSize().isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[] responsePixels = new int[getSize().getHight() * getSize().getWidth()];

        System.arraycopy(menuBackground, 0, responsePixels, 0, menuBackground.length);

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.length, //
                componentPixels.getPixels().length//
        );

        return new DrawableImpl(responsePixels, getSize());
    }

    private static int[] generateMenuBackground(int w, int h) {
        if (0 == w || 0 == h) {
            return EMPTY_MENUBAR;
        }

        int[] responsePixels = new int[h * w];
        Arrays.fill(responsePixels, MENUBAR_BACKGROUND);
        return responsePixels;
    }

    @Override
    public void resizeTo(Size newSize) {
        if (getSize().equals(newSize)) {
            return;
        }
        Size oldSize = this.getSize();
        setSize(newSize);

        computePixels();

        controller.propertyChange(new PropertyChangeEvent(//
                this, //
                ViewPropertyChangeType.UPDATE.getTypeName(), //
                oldSize, //
                newSize//
        ));
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseY <= MENUBAR_HIGHT) {
            LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY, mouseX,
                    mouseY - MENUBAR_HIGHT);
            if (component instanceof MouseClickable) {
                MouseClickable comp = (MouseClickable) component;
                comp.onMouseClick(mouseX, mouseY - MENUBAR_HIGHT, button);
            }
        }
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("Menubar send Update to window");
        controller.propertyChange(new PropertyChangeEvent(//
                this, //
                ViewPropertyChangeType.UPDATE.getTypeName(), //
                1, //
                2 //
        ));
    }

    @Override
    public void onKeyTyped(Character key) {
        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyTyped(key);
        }
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyPressed(keyCode);
        }
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyReleased(keyCode);
        }
    }

}
