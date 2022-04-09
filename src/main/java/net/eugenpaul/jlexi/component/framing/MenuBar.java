package net.eugenpaul.jlexi.component.framing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.component.interfaces.Resizeable;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph implements GuiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBar.class);

    private static final int[] EMPTY_MENUBAR = new int[0];
    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    private int[] menuBackground;
    private AbstractController controller;

    private Color backgroundColor = Color.WHITE;
    private int menubarHeight = 0;
    private int menubarPadding = 2;

    private List<Button> menuButtons;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBar(Glyph parent, Glyph component, Size size, AbstractController controller) {
        super(parent, component);
        component.setParent(this);
        component.setRelativPosition(new Vector2d(0, menubarHeight));
        setSize(size);
        this.controller = controller;
        this.menuButtons = new LinkedList<>();

        computeBackground();
    }

    public boolean addMenuButton(Button button) {
        menuButtons.add(button);

        menubarHeight = menuButtons.stream().map(v -> v.getSize().getHeight()).reduce(0, Math::max);
        if (menubarHeight > 0) {
            menubarHeight += menubarPadding * 2;
        }

        component.setRelativPosition(new Vector2d(0, menubarHeight));

        computeBackground();
        return true;
    }

    private void computeBackground() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.resizeTo(getSize().getWidth(), Math.max(0, getSize().getHeight() - menubarHeight));
        }

        this.menuBackground = generateMenuBackground(//
                getSize().getWidth(), //
                Math.min(getSize().getHeight(), menubarHeight)//
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

        cachedDrawable = new DrawableImpl(responsePixels, getSize());

        Vector2d pos = new Vector2d(menubarPadding, menubarPadding);
        for (Button button : menuButtons) {
            ImageArrayHelper.copyRectangle(//
                    button.getPixels(), //
                    cachedDrawable, //
                    pos//
            );
            button.setRelativPosition(new Vector2d(pos));
            pos.setX(pos.getX() + button.getSize().getWidth() + menubarPadding);
        }

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.length, //
                componentPixels.getPixels().length//
        );

        return cachedDrawable;
    }

    private int[] generateMenuBackground(int w, int h) {
        if (0 == w || 0 == h) {
            return EMPTY_MENUBAR;
        }

        int[] responsePixels = new int[h * w];
        Arrays.fill(responsePixels, backgroundColor.getARGB());
        return responsePixels;
    }

    @Override
    public void resizeTo(Size newSize) {
        setSize(newSize);

        computeBackground();

        getPixels();

        notifyRedraw(cachedDrawable, relativPosition, newSize);
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseY <= menubarHeight) {
            LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
            for (Button menuButton : menuButtons) {
                if (CollisionHelper.isPointOnArea(mouseX, mouseY, menuButton.getRelativPosition(),
                        menuButton.getSize())) {
                    menuButton.onMouseClick(//
                            mouseX - menuButton.getRelativPosition().getX(), //
                            mouseY - menuButton.getRelativPosition().getY(), //
                            button //
                    );
                    return;
                }
            }
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY, mouseX,
                    mouseY - menubarHeight);
            if (component instanceof MouseClickable) {
                MouseClickable comp = (MouseClickable) component;
                comp.onMouseClick(mouseX, mouseY - menubarHeight, button);
            }
        }
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

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

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        return component.getPixels(new Vector2d(position.getX(), position.getY() - menubarHeight), size);
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        if (parent != null) {
            parent.notifyRedraw(drawData, relativPosition.addNew(this.relativPosition), size);
        }
    }

    @Override
    public Glyph getMainGlyph() {
        return this;
    }
}
