package net.eugenpaul.jlexi.data.framing;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.model.InterfaceModel;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph implements Resizeable, InterfaceModel {

    private static final int MENUBAR_HIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    /** width of the window */
    private int width;
    /** hight of the window */
    private int hight;

    private int[][] menuBackground;
    private AbstractController controller;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public MenuBar(Glyph component, int width, int hight, AbstractController controller) {
        super(component);
        this.width = width;
        this.hight = hight;
        this.controller = controller;

        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(width, hight - MENUBAR_HIGHT);
        }

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

    @Override
    public void setSize(int width, int hight) {
        this.width = width;
        this.hight = hight;

        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(width, hight - MENUBAR_HIGHT);
        }

        this.menuBackground = generateMenuBackground(width, MENUBAR_HIGHT);
    }

    @Override
    public void setSize(Size size) {
        setSize(size.getWidth(), size.getHight());

        controller.propertyChange(new PropertyChangeEvent(this, "UPDATE", null, size));
    }

}
