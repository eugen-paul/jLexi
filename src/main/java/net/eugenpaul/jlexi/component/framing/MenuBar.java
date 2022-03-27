package net.eugenpaul.jlexi.component.framing;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.component.interfaces.Resizeable;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.RedrawData;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph implements GuiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBar.class);

    private static final int MENUBAR_HEIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    private static final int[] EMPTY_MENUBAR = new int[0];
    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    private int[] menuBackground;
    private AbstractController controller;

    private final String name;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBar(String name, Glyph parent, Glyph component, Size size, AbstractController controller) {
        super(parent, component);
        this.name = name;
        component.setParent(this);
        component.setRelativPosition(new Vector2d(0, MENUBAR_HEIGHT));
        setSize(size);
        this.controller = controller;

        computePixels();
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.resizeTo(name, getSize().getWidth(), Math.max(0, getSize().getHeight() - MENUBAR_HEIGHT));
        }

        this.menuBackground = generateMenuBackground(//
                getSize().getWidth(), //
                Math.min(getSize().getHeight(), MENUBAR_HEIGHT)//
        );
    }

    @Override
    public Drawable getPixels() {
        if (getSize().isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[] responsePixels = new int[getSize().getHeight() * getSize().getWidth()];

        System.arraycopy(menuBackground, 0, responsePixels, 0, menuBackground.length);

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.length, //
                componentPixels.getPixels().length//
        );

        cachedDrawable = new DrawableImpl(responsePixels, getSize());

        return cachedDrawable;
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
    public void resizeTo(String name, Size newSize) {
        if (!name.equals(this.name) || getSize().equals(newSize)) {
            return;
        }

        setSize(newSize);

        computePixels();

        getPixels();

        notifyRedraw(cachedDrawable, relativPosition, newSize);
    }

    @Override
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (!name.equals(this.name)) {
            return;
        }

        if (mouseY <= MENUBAR_HEIGHT) {
            LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY, mouseX,
                    mouseY - MENUBAR_HEIGHT);
            if (component instanceof MouseClickable) {
                MouseClickable comp = (MouseClickable) component;
                comp.onMouseClick(name, mouseX, mouseY - MENUBAR_HEIGHT, button);
            }
        }
    }

    @Override
    public void onKeyTyped(String name, Character key) {
        if (!name.equals(this.name)) {
            return;
        }

        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyTyped(name, key);
        }
    }

    @Override
    public void onKeyPressed(String name, KeyCode keyCode) {
        if (!name.equals(this.name)) {
            return;
        }

        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyPressed(name, keyCode);
        }
    }

    @Override
    public void onKeyReleased(String name, KeyCode keyCode) {
        if (!name.equals(this.name)) {
            return;
        }

        if (component instanceof KeyPressable) {
            KeyPressable comp = (KeyPressable) component;
            comp.onKeyReleased(name, keyCode);
        }
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        return component.getPixels(new Vector2d(position.getX(), position.getY() - MENUBAR_HEIGHT), size);
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        LOGGER.trace("Menubar send notifyRedraw Data to window");

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

    @Override
    public Glyph getMainGlyph() {
        return this;
    }
}
