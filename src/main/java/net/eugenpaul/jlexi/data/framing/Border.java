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

    private Size size;
    private int borderSize = BORDER_SIZE;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public Border(Glyph component) {
        super(component);
        size = Size.ZERO_SIZE;
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(//
                    Math.max(0, size.getWidth() - borderSize * 2), //
                    Math.max(0, size.getHight() - borderSize * 2)//
            );
        }
    }

    @Override
    public Drawable getPixels() {
        if (size.getHight() <= borderSize * 2 //
                || size.getWidth() <= borderSize * 2 //
        ) {
            return new DrawableImpl(generateBlackBorder(), size);
        }

        Drawable childDraw = super.getPixels();

        int[] componentPixels = childDraw.getPixels();
        int[] borderPixels = new int[size.getHight() * size.getWidth()];

        if (borderSize > 0) {
            Arrays.fill(borderPixels, 0, size.getWidth() * borderSize, BORDER_BLACK);
        }

        ImageArrays.copyRectangle(//
                componentPixels, //
                childDraw.getPixelSize(), //
                borderPixels, //
                size, //
                borderSize, //
                borderSize //
        );

        if (borderSize > 0) {
            int targetPosition = size.getWidth() * borderSize - borderSize;
            for (int i = borderSize; i < size.getHight() - borderSize + 1; i++) {
                Arrays.fill(borderPixels, targetPosition, targetPosition + borderSize * 2, BORDER_BLACK);
                targetPosition += size.getWidth();
            }
        }

        if (borderSize > 0) {
            Arrays.fill(//
                    borderPixels, //
                    borderPixels.length - 1 - size.getWidth() * borderSize, //
                    borderPixels.length, //
                    BORDER_BLACK//
            );
        }

        return new DrawableImpl(borderPixels, size);
    }

    private int[] generateBlackBorder() {
        int[] responsePixels = new int[size.getHight() * size.getWidth()];
        Arrays.fill(responsePixels, BORDER_BLACK);
        return responsePixels;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
        computePixels();
    }
}
