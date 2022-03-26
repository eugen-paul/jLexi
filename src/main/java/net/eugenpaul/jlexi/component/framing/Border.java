package net.eugenpaul.jlexi.component.framing;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.component.interfaces.Resizeable;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

/**
 * Glyph with a boarder.
 */
public class Border extends MonoGlyph implements GuiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Border.class);

    private static final int BORDER_BLACK = 0xFF000000;
    private static final int BORDER_SIZE = 2;

    private int borderSize = BORDER_SIZE;

    private String name;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public Border(String name, Glyph parent, Glyph component) {
        super(parent, component);
        this.name = name;
        component.setParent(this);
        component.setRelativPosition(new Vector2d(borderSize, borderSize));
        resizeTo(this.name, Size.ZERO_SIZE);
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.resizeTo(//
                    this.name, //
                    Math.max(0, getSize().getWidth() - borderSize * 2), //
                    Math.max(0, getSize().getHeight() - borderSize * 2)//
            );
        }
    }

    @Override
    public Drawable getPixels() {
        if (getSize().getHeight() <= borderSize * 2 //
                || getSize().getWidth() <= borderSize * 2 //
        ) {
            cachedDrawable = new DrawableImpl(generateBlackBorder(), getSize());
            return cachedDrawable;
        }

        Drawable childDraw = super.getPixels();

        int[] componentPixels = childDraw.getPixels();
        int[] borderPixels = new int[getSize().getHeight() * getSize().getWidth()];

        if (borderSize > 0) {
            Arrays.fill(borderPixels, 0, getSize().getWidth() * borderSize, BORDER_BLACK);
        }

        ImageArrayHelper.copyRectangle(//
                componentPixels, //
                childDraw.getPixelSize(), //
                new Vector2d(0, 0), //
                childDraw.getPixelSize(), //
                borderPixels, //
                getSize(), //
                new Vector2d(borderSize, borderSize) //
        );

        if (borderSize > 0) {
            int targetPosition = getSize().getWidth() * borderSize - borderSize;
            for (int i = borderSize; i < getSize().getHeight() - borderSize + 1; i++) {
                Arrays.fill(borderPixels, targetPosition, targetPosition + borderSize * 2, BORDER_BLACK);
                targetPosition += getSize().getWidth();
            }
        }

        if (borderSize > 0) {
            Arrays.fill(//
                    borderPixels, //
                    borderPixels.length - 1 - getSize().getWidth() * borderSize, //
                    borderPixels.length, //
                    BORDER_BLACK//
            );
        }

        cachedDrawable = new DrawableImpl(borderPixels, getSize());
        return cachedDrawable;
    }

    private int[] generateBlackBorder() {
        int[] responsePixels = new int[getSize().getHeight() * getSize().getWidth()];
        Arrays.fill(responsePixels, BORDER_BLACK);
        return responsePixels;
    }

    @Override
    public void resizeTo(String name, Size size) {
        if (!name.equals(this.name)) {
            return;
        }
        setSize(size);
        computePixels();
        getPixels();
    }

    @Override
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (!name.equals(this.name)) {
            return;
        }

        if (mouseX < BORDER_SIZE //
                || mouseX > getSize().getWidth() - BORDER_SIZE //
                || mouseY < BORDER_SIZE //
                || mouseY > getSize().getHeight() - BORDER_SIZE //
        ) {
            LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY,
                    mouseX - BORDER_SIZE, mouseY - BORDER_SIZE);
            if (component instanceof MouseClickable) {
                MouseClickable comp = (MouseClickable) component;
                comp.onMouseClick(name, mouseX - BORDER_SIZE, mouseY - BORDER_SIZE, button);
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
        return component.getPixels(new Vector2d(position.getX() - borderSize, position.getY() - borderSize), size);
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        if (parent != null) {
            parent.notifyRedraw(drawData, relativPosition.addNew(this.relativPosition), size);
        }
    }

}
