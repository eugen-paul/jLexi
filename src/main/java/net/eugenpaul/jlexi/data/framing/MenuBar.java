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
public class MenuBar extends MonoGlyph implements Resizeable, InterfaceModel, MouseClickable {

    private static final int MENUBAR_HIGHT = 40;
    private static final int MENUBAR_BACKGROUND = 0xFF00FF00;

    private static final int[] EMPTY_MENUBAR = new int[0];
    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    private int[] menuBackground;
    private AbstractController controller;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBar(Glyph parent, Glyph component, Size size, AbstractController controller) {
        super(parent, component);
        component.setParent(this);
        setPreferredSize(size);
        this.controller = controller;

        computePixels();
    }

    private void computePixels() {
        if (component instanceof Resizeable) {
            Resizeable child = (Resizeable) component;
            child.setSize(getPreferredSize().getWidth(), Math.max(0, getPreferredSize().getHight() - MENUBAR_HIGHT));
        }

        this.menuBackground = generateMenuBackground(//
                getPreferredSize().getWidth(), //
                Math.min(getPreferredSize().getHight(), MENUBAR_HIGHT)//
        );
    }

    @Override
    public Drawable getPixels() {
        if (getPreferredSize().isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[] responsePixels = new int[getPreferredSize().getHight() * getPreferredSize().getWidth()];

        System.arraycopy(menuBackground, 0, responsePixels, 0, menuBackground.length);

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.length, //
                componentPixels.getPixels().length//
        );

        return new DrawableImpl(responsePixels, getPreferredSize());
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
    public void setSize(Size newSize) {
        if (getPreferredSize().equals(newSize)) {
            return;
        }
        Size oldSize = this.getPreferredSize();
        setPreferredSize(newSize);

        computePixels();

        controller.propertyChange(new PropertyChangeEvent(//
                this, //
                ViewPropertyChangeType.UPDATE.getTypeName(), //
                oldSize, //
                newSize//
        ));
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseY > MENUBAR_HIGHT) {
            System.out.println("Click not on Menu with " + button.toString());
        } else {
            System.out.println("Click on Menu with " + button.toString());
        }
    }

}
