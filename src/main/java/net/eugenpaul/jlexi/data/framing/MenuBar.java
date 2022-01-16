package net.eugenpaul.jlexi.data.framing;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.model.InterfaceModel;

/**
 * Add Menubar to Glyph
 */
public class MenuBar extends MonoGlyph implements Resizeable, InterfaceModel {

    private static final int MENUBAR_HIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    private static final int[] EMPTY_MENUBAR = new int[0];
    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    private Size size;

    private int[] menuBackground;
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

        computePixels();
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(size.getWidth(), Math.max(0, size.getHight() - MENUBAR_HIGHT));
        }

        this.menuBackground = generateMenuBackground(//
                size.getWidth(), //
                Math.min(size.getHight(), MENUBAR_HIGHT)//
        );
    }

    @Override
    public Drawable getPixels() {
        if (size.isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[] responsePixels = new int[size.getHight() * size.getWidth()];

        System.arraycopy(menuBackground, 0, responsePixels, 0, menuBackground.length);

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.length, //
                componentPixels.getPixels().length//
        );

        return new DrawableImpl(responsePixels, size);
    }

    private static int[] generateMenuBackground(int w, int h) {
        if (0 == w || 0 == h) {
            return EMPTY_MENUBAR;
        }

        int[] responsePixels = new int[h * w];
        Arrays.fill(responsePixels, MENUBAR_BACKGROUND);
        return responsePixels;
    }

    @Override
    public Size getSize() {
        return new Size(size);
    }

    @Override
    public void setSize(Size newSize) {
        if (this.size.equals(newSize)) {
            return;
        }
        Size oldSize = this.size;
        this.size = newSize;

        computePixels();

        controller.propertyChange(new PropertyChangeEvent(//
                this, //
                ViewPropertyChangeType.UPDATE.getTypeName(), //
                oldSize, //
                newSize//
        ));
    }

}
