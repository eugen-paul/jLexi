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

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public Border(Glyph parent, Glyph component) {
        super(parent, component);
        component.setParent(this);
        resizeTo(Size.ZERO_SIZE);
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.resizeTo(//
                    Math.max(0, getSize().getWidth() - borderSize * 2), //
                    Math.max(0, getSize().getHight() - borderSize * 2)//
            );
        }
    }

    @Override
    public Drawable getPixels() {
        if (getSize().getHight() <= borderSize * 2 //
                || getSize().getWidth() <= borderSize * 2 //
        ) {
            return new DrawableImpl(generateBlackBorder(), getSize());
        }

        Drawable childDraw = super.getPixels();

        int[] componentPixels = childDraw.getPixels();
        int[] borderPixels = new int[getSize().getHight() * getSize().getWidth()];

        if (borderSize > 0) {
            Arrays.fill(borderPixels, 0, getSize().getWidth() * borderSize, BORDER_BLACK);
        }

        ImageArrayHelper.copyRectangle(//
                componentPixels, //
                childDraw.getPixelSize(), //
                0, //
                0, //
                childDraw.getPixelSize(), //
                borderPixels, //
                getSize(), //
                borderSize, //
                borderSize //
        );

        if (borderSize > 0) {
            int targetPosition = getSize().getWidth() * borderSize - borderSize;
            for (int i = borderSize; i < getSize().getHight() - borderSize + 1; i++) {
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

        return new DrawableImpl(borderPixels, getSize());
    }

    private int[] generateBlackBorder() {
        int[] responsePixels = new int[getSize().getHight() * getSize().getWidth()];
        Arrays.fill(responsePixels, BORDER_BLACK);
        return responsePixels;
    }

    @Override
    public void resizeTo(Size size) {
        setSize(size);
        computePixels();
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseX < BORDER_SIZE //
                || mouseX > getSize().getWidth() - BORDER_SIZE //
                || mouseY < BORDER_SIZE //
                || mouseY > getSize().getHight() - BORDER_SIZE //
        ) {
            LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY,
                    mouseX - BORDER_SIZE, mouseY - BORDER_SIZE);
            if (component instanceof MouseClickable) {
                MouseClickable comp = (MouseClickable) component;
                comp.onMouseClick(mouseX - BORDER_SIZE, mouseY - BORDER_SIZE, button);
            }
        }
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("Border notifyUpdate to parent");
        getParent().notifyUpdate(this);
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