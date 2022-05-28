package net.eugenpaul.jlexi.component.scrollpane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.ScrollGlypthCompositor;
import net.eugenpaul.jlexi.component.scrollpane.Scrollbar.ScrollbarType;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

/**
 * GuiGlyph with a Scrollpane.
 */
public class Scrollpane extends GuiCompenentMonoGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scrollpane.class);

    private static final int V_BAR_WIDTH = 11;
    private static final int H_BAR_HEIGHT = 11;

    private Color scrollbarColor;
    private Color backgroundColor;

    private ScrollGlypthCompositor<Glyph> compositor;

    private Scrollbar vBar;
    private Scrollbar hBar;

    private int vBarWidth = V_BAR_WIDTH;
    private int hBarHeight = H_BAR_HEIGHT;

    private ScrollbarShowType showVBar = ScrollbarShowType.AT_NEED;
    private ScrollbarShowType showHBar = ScrollbarShowType.AT_NEED;

    private int vScrollPosition = 0;
    private int hScrollPosition = 0;

    /**
     * C'tor
     * 
     * @param component component that will be scrolled.
     */
    private Scrollpane(Glyph parent, GuiGlyph component, Color scrollbarColor, Color backgroundColor) {
        super(parent, component);
        this.scrollbarColor = scrollbarColor;
        this.backgroundColor = backgroundColor;

        this.compositor = new ScrollGlypthCompositor<>(backgroundColor, vScrollPosition, hScrollPosition);

        this.component.setRelativPosition(new Vector2d(0, 0));
        resizeTo(Size.ZERO_SIZE);
    }

    public static ScrollpaneBuilder builder() {
        return new ScrollpaneBuilder();
    }

    public static class ScrollpaneBuilder {
        private Glyph parent;
        private GuiGlyph component;
        private Color scrollbarColor;
        private Color backgroundColor;

        private GuiFactory factory;

        private ScrollbarShowType showVBar = ScrollbarShowType.AT_NEED;
        private ScrollbarShowType showHBar = ScrollbarShowType.AT_NEED;

        private ScrollpaneBuilder() {

        }

        public ScrollpaneBuilder factory(GuiFactory factory) {
            this.factory = factory;
            return this;
        }

        public ScrollpaneBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public ScrollpaneBuilder component(GuiGlyph component) {
            this.component = component;
            return this;
        }

        public ScrollpaneBuilder scrollbarColor(Color scrollbarColor) {
            this.scrollbarColor = scrollbarColor;
            return this;
        }

        public ScrollpaneBuilder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Scrollpane build() {
            Scrollpane response = new Scrollpane(parent, component, scrollbarColor, backgroundColor);

            response.vBar = ScrollbarImage.builder() //
                    .factory(factory)//
                    .type(ScrollbarType.VERTICAL) //
                    .width(V_BAR_WIDTH) //
                    .parent(parent) //
                    .build();

            response.hBar = ScrollbarImage.builder() //
                    .factory(factory)//
                    .type(ScrollbarType.HORIZONTAL) //
                    .width(H_BAR_HEIGHT) //
                    .parent(parent) //
                    .build();

            response.showVBar = showVBar;
            response.showHBar = showHBar;

            return response;
        }
    }

    private void resizeComponent() {
        if (this.component.isResizeble()) {
            this.component.resizeTo(//
                    Math.max(0, getSize().getWidth() - this.vBarWidth), //
                    Math.max(0, getSize().getHeight() - this.hBarHeight) //
            );

            if (this.vBar != null) {
                if (isHBarVisible()) {
                    this.vBar.resizeTo(this.vBarWidth, Math.max(0, getSize().getHeight() - this.hBarHeight));
                } else {
                    this.vBar.resizeTo(this.vBarWidth, getSize().getHeight());
                }
            }
            if (this.hBar != null) {
                if (isVBarVisible()) {
                    this.hBar.resizeTo(Math.max(0, getSize().getWidth() - this.vBarWidth), this.hBarHeight);
                } else {
                    this.hBar.resizeTo(getSize().getWidth(), this.hBarHeight);
                }
            }
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

        if (!isHBarVisible()) {
            hScrollPosition = 0;
        }
        hBar.setIntervalOffset(hScrollPosition);
        vBar.setIntervalOffset(vScrollPosition);

        this.compositor.setHOffset(hScrollPosition);
        this.compositor.setVOffset(vScrollPosition);
        Glyph composedGlyphs = this.compositor.compose(component, childSize);

        this.cachedDrawable = new DrawableSketchImpl(backgroundColor);
        var childDraw = composedGlyphs.getDrawable();

        this.cachedDrawable.addDrawable(childDraw, 0, 0, 0);

        addVScrollbarToCachedDrawable();
        addHScrollbarToCachedDrawable();
        addScrollbarDeadSpace();

        return this.cachedDrawable.draw();
    }

    private boolean isVBarVisible() {
        if (showVBar == ScrollbarShowType.ALWAYS) {
            return true;
        }
        if (showVBar == ScrollbarShowType.NOT) {
            return false;
        }
        int componentHeight = component.getDrawable().getSize().getHeight();
        int paneHeight = getSize().getHeight();
        return paneHeight < componentHeight;
    }

    private void addVScrollbarToCachedDrawable() {
        if (!isVBarVisible()) {
            return;
        }

        int componentHeight = component.getDrawable().getSize().getHeight();
        int paneHeight = getSize().getHeight();
        vBar.setIntervalTotal(componentHeight);
        vBar.setIntervalDisplayed(paneHeight);
        vBar.setIntervalOffset(vScrollPosition * (-1L));

        this.cachedDrawable.addDrawable(//
                vBar.getDrawable(), //
                getSize().getWidth() - this.vBarWidth, //
                0, //
                1 //
        );
    }

    private void addScrollbarDeadSpace() {
        if (!isVBarVisible() || !isHBarVisible()) {
            return;
        }

        DrawableSketch bar = new DrawableSketchImpl(//
                scrollbarColor, //
                new Size(//
                        this.vBarWidth, //
                        this.hBarHeight //
                )//
        );

        this.cachedDrawable.addDrawable(//
                bar.draw(), //
                getSize().getWidth() - this.vBarWidth, //
                getSize().getHeight() - this.hBarHeight, //
                1 //
        );
    }

    private boolean isHBarVisible() {
        if (showHBar == ScrollbarShowType.ALWAYS) {
            return true;
        }
        if (showHBar == ScrollbarShowType.NOT) {
            return false;
        }
        int componentWidth = component.getDrawable().getSize().getWidth();
        int paneWidth = getSize().getWidth();
        return paneWidth < componentWidth;
    }

    private void addHScrollbarToCachedDrawable() {
        if (!isHBarVisible()) {
            return;
        }

        int componentWidth = component.getDrawable().getSize().getWidth();
        int paneWidth = getSize().getWidth();
        hBar.setIntervalTotal(componentWidth);
        hBar.setIntervalDisplayed(paneWidth);
        hBar.setIntervalOffset(hScrollPosition * (-1L));

        this.cachedDrawable.addDrawable(//
                hBar.getDrawable(), //
                0, //
                getSize().getHeight() - this.hBarHeight, //
                1 //
        );
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
        return !isClickOnVBar(mouseX) && !isClickOnHBar(mouseY);
    }

    private boolean isClickOnHBar(Integer mouseY) {
        return isHBarVisible() && mouseY > getSize().getHeight() - hBarHeight;
    }

    private boolean isClickOnVBar(Integer mouseX) {
        return isVBarVisible() && mouseX > getSize().getWidth() - vBarWidth;
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Scrollbar. Position ({},{}).", mouseX, mouseY);

        if (isClickOnVBar(mouseX)) {
            int barHeight = compVBarHeight();
            if (mouseY < barHeight / 2) {
                scrollUp();
            } else {
                scrollDown();
            }
        } else if (isClickOnHBar(mouseY)) {
            int barWidth = compHBarWidth();
            if (mouseX < barWidth / 2) {
                hScrollPosition = Math.min(0, hScrollPosition + 50);
            } else {
                int componenttWidth = component.getDrawable().getSize().getWidth();
                int panetWidth = getSize().getWidth();
                hScrollPosition = Math.max(//
                        Math.min(0, -1 * (componenttWidth - panetWidth) - 50), //
                        hScrollPosition - 50 //
                );
            }
        }

        redraw();
    }

    private int compHBarWidth() {
        int barWidth = getSize().getWidth();

        if (isVBarVisible()) {
            barWidth -= this.vBarWidth;
        }
        return barWidth;
    }

    private int compVBarHeight() {
        int barHeight = getSize().getHeight();

        if (isHBarVisible()) {
            barHeight -= this.hBarHeight;
        }
        return barHeight;
    }

    private void scrollDown() {
        int componentHeight = component.getDrawable().getSize().getHeight();
        int paneHeight = getSize().getHeight();
        vScrollPosition = Math.max(//
                Math.min(0, -1 * (componentHeight - paneHeight) - 50), //
                vScrollPosition - 50 //
        );
    }

    private void scrollUp() {
        vScrollPosition = Math.min(0, vScrollPosition + 50);
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (direction == MouseWheelDirection.UP) {
            scrollUp();
        } else {
            scrollDown();
        }
        redraw();
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
