package net.eugenpaul.jlexi.data.framing;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
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

    private static final int[][] EMPTY_MENUBAR = new int[0][0];
    private static final Drawable EMPTY_DRAWABLE = () -> EMPTY_MENUBAR;

    private Size size;

    private int[][] menuBackground;
    private AbstractController controller;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBar(Glyph component, Size size, AbstractController controller) {
        super(component);
        this.size = size;
        this.controller = controller;

        computeBackground(component, size);
    }

    private void computeBackground(Glyph component, Size size) {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(size.getWidth(), Math.max(0, size.getHight() - MENUBAR_HIGHT));
        }

        this.menuBackground = generateMenuBackground(//
                size.getWidth(), //
                Math.min(size.getHight() - MENUBAR_HIGHT, MENUBAR_HIGHT)//
        );
    }

    @Override
    public Drawable getPixels() {
        if (size.isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[][] responsePixels = new int[size.getHight()][size.getWidth()];

        for (int i = 0; i < menuBackground.length; i++) {
            System.arraycopy(menuBackground[i], 0, responsePixels[i], 0, size.getWidth());
        }

        for (int i = 0; i < componentPixels.getPixels().length; i++) {
            System.arraycopy(componentPixels.getPixels()[i], 0, responsePixels[i + menuBackground.length], 0,
                    size.getWidth());
        }

        return () -> responsePixels;
    }

    private static int[][] generateMenuBackground(int w, int h) {
        if (0 == w || 0 == h) {
            return EMPTY_MENUBAR;
        }

        int[][] responsePixels = new int[h][w];
        for (int[] line : responsePixels) {
            Arrays.fill(line, MENUBAR_BACKGROUND);
        }
        return responsePixels;
    }

    @Override
    public Size getSize() {
        return new Size(size);
    }

    @Override
    public void setSize(int width, int hight) {
        if (size.getWidth() == width //
                && size.getHight() == hight //
        ) {
            return;
        }
        setSize(new Size(width, hight));
    }

    @Override
    public void setSize(Size newSize) {
        if (this.size.equals(newSize)) {
            return;
        }
        Size oldSize = this.size;
        this.size = newSize;

        computeBackground(component, newSize);

        controller.propertyChange(new PropertyChangeEvent(//
                this, //
                ViewPropertyChangeType.UPDATE.getTypeName(), //
                oldSize, //
                newSize//
        ));
    }

}
