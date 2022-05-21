package net.eugenpaul.jlexi.component.scrollpane;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.ScrollGlypthCompositor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

/**
 * Glyph with a Scrollpane.
 * 
 */
public class Scrollpane extends GuiCompenentMonoGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scrollpane.class);

    private static final int V_BAR_WIDTH = 11;
    private static final int H_BAR_HEIGHT = 11;

    private Color scrollbarColor;
    private Color backgroundColor;

    private ScrollGlypthCompositor<Glyph> compositor;

    private int vBarWidth = V_BAR_WIDTH;
    private int hBarHeight = H_BAR_HEIGHT;

    private boolean showVBar = true;
    private boolean showHBar = false;

    private int vScrollPosition = 0;
    private int hScrollPosition = 0;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public Scrollpane(Glyph parent, GuiGlyph component, Color scrollbarColor, Color backgroundColor) {
        super(parent, component);
        this.scrollbarColor = scrollbarColor;
        this.backgroundColor = backgroundColor;

        this.compositor = new ScrollGlypthCompositor<>(backgroundColor, vScrollPosition, hScrollPosition);
        if (!this.showVBar) {
            this.vBarWidth = 0;
        }

        if (!this.showHBar) {
            this.hBarHeight = 0;
        }
        this.component.setRelativPosition(new Vector2d(0, 0));
        resizeTo(Size.ZERO_SIZE);

    }

    private void resizeComponent() {
        if (component.isResizeble()) {
            component.resizeTo(//
                    Math.max(0, getSize().getWidth() - this.vBarWidth), //
                    Math.max(0, getSize().getHeight() - this.hBarHeight) //
            );
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.compositor.setBackgroundColor(backgroundColor);
        this.cachedDrawable = null;
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        if (getSize().getHeight() <= this.hBarHeight //
                || getSize().getWidth() <= this.vBarWidth //
        ) {
            this.cachedDrawable = new DrawableSketchImpl(scrollbarColor, getSize());
            return this.cachedDrawable.draw();
        }

        Size childSize = new Size(//
                Math.max(0, getSize().getWidth() - this.vBarWidth), //
                Math.max(0, getSize().getHeight() - this.hBarHeight) //
        );

        this.compositor.setHOffset(hScrollPosition);
        this.compositor.setVOffset(vScrollPosition);
        Glyph composedGlyphs = this.compositor.compose(component, childSize);

        this.cachedDrawable = new DrawableSketchImpl(backgroundColor);
        var childDraw = composedGlyphs.getDrawable();

        this.cachedDrawable.addDrawable(childDraw, 0, 0, 0);

        addVScrollbarToCachedDrawable();
        addHScrollbarToCachedDrawable();

        return this.cachedDrawable.draw();
    }

    private void addVScrollbarToCachedDrawable() {
        if (!this.showVBar) {
            return;
        }

        int[] barPixels = new int[this.vBarWidth * getSize().getHeight()];
        Arrays.fill(barPixels, 0, barPixels.length, scrollbarColor.getArgb());

        int[] arrowPixels = new int[this.vBarWidth * this.vBarWidth];
        Arrays.fill(arrowPixels, 0, arrowPixels.length, Color.YELLOW.getArgb());

        ImageArrayHelper.copyRectangle(//
                arrowPixels, //
                new Size(this.vBarWidth, this.vBarWidth), //
                new Vector2d(0, 0), //
                new Size(this.vBarWidth, this.vBarWidth), //
                barPixels, //
                new Size(this.vBarWidth, getSize().getHeight()), //
                new Vector2d(0, 0));

        ImageArrayHelper.copyRectangle(//
                arrowPixels, //
                new Size(this.vBarWidth, this.vBarWidth), //
                new Vector2d(0, 0), //
                new Size(this.vBarWidth, this.vBarWidth), //
                barPixels, //
                new Size(this.vBarWidth, getSize().getHeight()), //
                new Vector2d(0, getSize().getHeight() - this.vBarWidth));

        DrawablePixelsImpl barVertical = DrawablePixelsImpl.builderArgb()//
                .argbPixels(barPixels)//
                .size(new Size(this.vBarWidth, getSize().getHeight()))//
                .build();

        this.cachedDrawable.addDrawable(barVertical, getSize().getWidth() - this.vBarWidth, 0, 1);
    }

    private void addHScrollbarToCachedDrawable() {
        // TODO
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size size) {
        cachedDrawable = null;
        setSize(size);
        resizeComponent();
        getDrawable();
    }

    @Override
    protected boolean isPositionOnComponent(Integer mouseX, Integer mouseY) {
        return mouseX > 0 //
                && mouseX < getSize().getWidth() - vBarWidth //
                && mouseY > 0 //
                && mouseY < getSize().getHeight() - hBarHeight //
        ;
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
        if (mouseY < getSize().getHeight() / 2) {
            LOGGER.trace("Scroll UP.");
            vScrollPosition += 50;
            redraw();
        } else if (mouseY >= getSize().getHeight() / 2) {
            LOGGER.trace("Scroll DOWN.");
            vScrollPosition -= 50;
            redraw();
        }
    }

    @Override
    protected void onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Pressed on Border. Position ({},{}).", mouseX, mouseY);
    }

    @Override
    protected void onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Released on Border. Position ({},{}).", mouseX, mouseY);
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }
}
