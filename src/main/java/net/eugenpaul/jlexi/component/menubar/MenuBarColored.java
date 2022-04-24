package net.eugenpaul.jlexi.component.menubar;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

/**
 * @param <T>
 * 
 */
public class MenuBarColored extends MenuBar {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBarColored.class);

    private Color backgroundColor;

    /**
     * C'tor
     * 
     * @param component
     */
    public MenuBarColored(Glyph parent, GuiGlyph component, Size size, Color backgroundColor) {
        super(parent, component, size);
        this.backgroundColor = backgroundColor;
    }

    @Override
    protected void computeBackground() {
        if (component.isResizeble()) {
            component.resizeTo(getSize().getWidth(), Math.max(0, getSize().getHeight() - menubarHeight));
        }

        this.menuBackground = generateMenuBackground(//
                getSize().getWidth(), //
                Math.min(getSize().getHeight(), menubarHeight)//
        );
    }

    private Drawable generateMenuBackground(int w, int h) {
        if (0 == w || 0 == h) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        int[] responsePixels = new int[h * w];
        Arrays.fill(responsePixels, backgroundColor.getArgb());
        return new DrawableImpl(responsePixels, new Size(w, h));
    }

}
