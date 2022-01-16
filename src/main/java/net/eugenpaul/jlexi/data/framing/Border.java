package net.eugenpaul.jlexi.data.framing;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;

/**
 * Glyph with a boarder.
 */
public class Border extends MonoGlyph implements Resizeable {

    private static final int BORDER_BLACK = 0xFF000000;
    private static final int BORDER_SIZE = 2;

    private Size size;

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
                    Math.max(0, size.getWidth() - BORDER_SIZE * 2), //
                    Math.max(0, size.getHight() - BORDER_SIZE * 2)//
            );
        }
    }

    @Override
    public Drawable getPixels() {
        if (size.getHight() <= BORDER_SIZE * 2 //
                || size.getWidth() <= BORDER_SIZE * 2 //
        ) {
            return this::generateBlackBorder;
        }

        Drawable childDraw = super.getPixels();

        int[][] componentPixels = childDraw.getPixels();
        int[][] borderPixels = new int[size.getHight()][size.getWidth()];

        for (int i = 0; i < BORDER_SIZE; i++) {
            Arrays.fill(borderPixels[i], BORDER_BLACK);
        }

        for (int i = 0; i < componentPixels.length; i++) {
            int[] childLine = componentPixels[i];

            int[] borderLine = borderPixels[i + BORDER_SIZE];
            for (int j = 0; j < BORDER_SIZE; j++) {
                borderLine[j] = BORDER_BLACK;

                borderLine[borderLine.length - 1 - j] = BORDER_BLACK;
            }

            System.arraycopy(childLine, 0, borderLine, BORDER_SIZE, childLine.length);
        }

        for (int i = 0; i < BORDER_SIZE; i++) {
            Arrays.fill(borderPixels[borderPixels.length - 1 - i], BORDER_BLACK);
        }

        return () -> borderPixels;
    }

    private int[][] generateBlackBorder() {
        int[][] responsePixels = new int[size.getHight()][size.getWidth()];
        for (int[] line : responsePixels) {
            Arrays.fill(line, BORDER_BLACK);
        }
        return responsePixels;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
        computePixels();
    }
}
