package net.eugenpaul.jlexi.data.framing;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.utils.ImageArrays;

/**
 * Glyph with a boarder.
 */
public class Border extends MonoGlyph implements Resizeable {

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
        setPreferredSize(Size.ZERO_SIZE);
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(//
                    Math.max(0, getPreferredSize().getWidth() - borderSize * 2), //
                    Math.max(0, getPreferredSize().getHight() - borderSize * 2)//
            );
        }
    }

    @Override
    public Drawable getPixels() {
        if (getPreferredSize().getHight() <= borderSize * 2 //
                || getPreferredSize().getWidth() <= borderSize * 2 //
        ) {
            return new DrawableImpl(generateBlackBorder(), getPreferredSize());
        }

        Drawable childDraw = super.getPixels();

        int[] componentPixels = childDraw.getPixels();
        int[] borderPixels = new int[getPreferredSize().getHight() * getPreferredSize().getWidth()];

        if (borderSize > 0) {
            Arrays.fill(borderPixels, 0, getPreferredSize().getWidth() * borderSize, BORDER_BLACK);
        }

        ImageArrays.copyRectangle(//
                componentPixels, //
                childDraw.getPixelSize(), //
                0, //
                0, //
                childDraw.getPixelSize(), //
                borderPixels, //
                getPreferredSize(), //
                borderSize, //
                borderSize //
        );

        if (borderSize > 0) {
            int targetPosition = getPreferredSize().getWidth() * borderSize - borderSize;
            for (int i = borderSize; i < getPreferredSize().getHight() - borderSize + 1; i++) {
                Arrays.fill(borderPixels, targetPosition, targetPosition + borderSize * 2, BORDER_BLACK);
                targetPosition += getPreferredSize().getWidth();
            }
        }

        if (borderSize > 0) {
            Arrays.fill(//
                    borderPixels, //
                    borderPixels.length - 1 - getPreferredSize().getWidth() * borderSize, //
                    borderPixels.length, //
                    BORDER_BLACK//
            );
        }

        return new DrawableImpl(borderPixels, getPreferredSize());
    }

    private int[] generateBlackBorder() {
        int[] responsePixels = new int[getPreferredSize().getHight() * getPreferredSize().getWidth()];
        Arrays.fill(responsePixels, BORDER_BLACK);
        return responsePixels;
    }

    @Override
    public void setSize(Size size) {
        setPreferredSize(size);
        computePixels();
    }

}
