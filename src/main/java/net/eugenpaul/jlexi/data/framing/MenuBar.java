package net.eugenpaul.jlexi.data.framing;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph {

    private static final int MENUBAR_HIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    /** width of the window */
    private int width;
    /** hight of the window */
    private int hight;

    private int[][] menuBackground;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public MenuBar(Glyph component, int width, int hight) {
        super(component);
        this.width = width;
        this.hight = hight;

        this.menuBackground = generateMenuBackground(width, MENUBAR_HIGHT);
    }

    @Override
    public Drawable getPixels() {
        Drawable componentPixels = super.getPixels();

        int[][] responsePixels = new int[hight][width];

        for (int i = 0; i < menuBackground.length; i++) {
            System.arraycopy(menuBackground[i], 0, responsePixels[i], 0, width);
        }

        for (int i = 0; i < componentPixels.getPixels().length; i++) {
            System.arraycopy(componentPixels.getPixels()[i], 0, responsePixels[i + menuBackground.length], 0, width);
        }

        return () -> responsePixels;
    }

    private int[][] generateMenuBackground(int w, int h) {
        int[][] responsePixels = new int[h][w];
        for (int[] line : responsePixels) {
            Arrays.fill(line, MENUBAR_BACKGROUND);
        }
        return responsePixels;
    }

    @Override
    public Size getSize() {
        return new Size(width, hight);
    }

}
