package net.eugenpaul.jlexi.data.framing;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.data.window.Window;

/**
 * Glyph with a boarder.
 */
public class Border extends MonoGlyph {

    private static final int BORDER_BLACK = 0xFFFFFF;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    protected Border(Glyph component) {
        super(component);
    }

    @Override
    public Drawable draw(Window window) {
        return null;
        // Drawable childDraw = super.draw();
        // int[][] componentPixels = childDraw.getPixels();
        // int[][] borderPixels = new int[componentPixels.length + 2][componentPixels[0].length + 2];

        // Arrays.fill(borderPixels[0], BORDER_BLACK);

        // for (int i = 0; i < componentPixels.length; i++) {
        //     int[] childLine = componentPixels[i];
        //     int[] borderLine = borderPixels[i + 1];
        //     borderLine[0] = BORDER_BLACK;

        //     System.arraycopy(childLine, 0, borderLine, 1, childLine.length);

        //     borderLine[borderLine.length - 1] = BORDER_BLACK;
        // }

        // Arrays.fill(borderPixels[borderPixels.length - 1], BORDER_BLACK);

        // return new Drawable() {

        //     @Override
        //     public int[][] getPixels() {
        //         return borderPixels;
        //     }

        //     @Override
        //     public Bounds getBounds() {
        //         Bounds childBounds = childDraw.getBounds();
        //         return new Bounds(//
        //                 childBounds.getP1X() - 1, //
        //                 childBounds.getP1Y() - 1, //
        //                 childBounds.getP2X() - 1, //
        //                 childBounds.getP2Y() - 1//
        //         );

        //     }

        // };
    }

    @Override
    public GlyphIterator createIterator() {
        // TODO Auto-generated method stub
        return null;
    }
}
