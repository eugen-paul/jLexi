package net.eugenpaul.jlexi.component.scrollpane;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.ScrollGlypthCompositor;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.scrollpane.Scrollbar.ScrollbarType;
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
@Slf4j
public abstract class Scrollpane extends GuiCompenentMonoGlyph {

    private static final Color SCROLLBAR_COLOR_DEFAULT = Color.GREY;
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    @Setter
    private Color scrollbarColor = SCROLLBAR_COLOR_DEFAULT;
    private Color backgroundColor = BACKGROUND_COLOR;

    private ScrollGlypthCompositor<Glyph> compositor;

    private Scrollbar vBar;
    private Scrollbar hBar;

    @Setter(value = AccessLevel.PROTECTED)
    private ScrollbarShowType showVBar = ScrollbarShowType.AT_NEED;
    @Setter(value = AccessLevel.PROTECTED)
    private ScrollbarShowType showHBar = ScrollbarShowType.AT_NEED;

    private int vScrollPosition = 0;
    private int hScrollPosition = 0;

    /**
     * C'tor
     * 
     * @param component component that will be scrolled.
     */
    protected Scrollpane(Glyph parent, GuiGlyph component, @NonNull Scrollbar vBar, @NonNull Scrollbar hBar) {
        super(parent, component);
        this.vBar = vBar;
        this.hBar = hBar;

        this.vBar.setParent(this);
        this.hBar.setParent(this);

        this.vBar.setScrollCallback(v -> this.scrollbarToCallback(v, ScrollbarType.VERTICAL));
        this.hBar.setScrollCallback(v -> this.scrollbarToCallback(v, ScrollbarType.HORIZONTAL));

        this.compositor = new ScrollGlypthCompositor<>(backgroundColor, vScrollPosition, hScrollPosition, true, true);

        this.component.setRelativPosition(new Vector2d(0, 0));
        addGuiChild(component);
        resizeTo(Size.ZERO_SIZE);
    }

    private void resizeComponent() {
        if (this.component.isResizeble()) {
            int vBarWidth = isVBarVisible() ? this.vBar.getSize().getWidth() : 0;
            int hBarHeight = isHBarVisible() ? this.hBar.getSize().getHeight() : 0;

            this.component.resizeTo(//
                    Math.max(0, getSize().getWidth() - vBarWidth), //
                    Math.max(0, getSize().getHeight() - hBarHeight) //
            );

            resizeBars(vBarWidth, hBarHeight);
        }
    }

    private void resizeBars(int vBarWidth, int hBarHeight) {
        if (isHBarVisible()) {
            this.vBar.resizeTo(vBarWidth, Math.max(0, getSize().getHeight() - hBarHeight));
        } else {
            this.vBar.resizeTo(vBarWidth, getSize().getHeight());
        }

        if (isVBarVisible()) {
            this.hBar.resizeTo(Math.max(0, getSize().getWidth() - vBarWidth), hBarHeight);
        } else {
            this.hBar.resizeTo(getSize().getWidth(), hBarHeight);
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

        int vBarWidth = isVBarVisible() ? this.vBar.getSize().getWidth() : 0;
        int hBarHeight = isHBarVisible() ? this.hBar.getSize().getHeight() : 0;

        resizeBars(vBarWidth, hBarHeight);

        if (getSize().getHeight() <= hBarHeight //
                || getSize().getWidth() <= vBarWidth //
        ) {
            this.cachedDrawable = new DrawableSketchImpl(scrollbarColor, getSize());
            return this.cachedDrawable.draw();
        }

        Size childSize = new Size(//
                Math.max(0, getSize().getWidth() - vBarWidth), //
                Math.max(0, getSize().getHeight() - hBarHeight) //
        );

        this.compositor.setCenterH(!isHBarVisible());
        this.compositor.setHOffset(this.hScrollPosition);

        this.compositor.setCenterV(!isVBarVisible());
        this.compositor.setVOffset(this.vScrollPosition);

        this.hBar.setIntervalOffset(this.hScrollPosition);
        this.vBar.setIntervalOffset(this.vScrollPosition);

        Glyph composedGlyphs = this.compositor.compose(this.component, childSize);

        this.cachedDrawable = new DrawableSketchImpl(this.backgroundColor);
        var childDraw = composedGlyphs.getDrawable();

        this.cachedDrawable.addDrawable(childDraw, 0, 0, 0);

        addVScrollbarToCachedDrawable();
        addHScrollbarToCachedDrawable();
        addScrollbarDeadSpace();

        return this.cachedDrawable.draw();
    }

    private void addVScrollbarToCachedDrawable() {
        if (!isVBarVisible()) {
            return;
        }

        int componentHeight = this.component.getSize().getHeight();
        int paneHeight = getSize().getHeight();
        this.vBar.setIntervalTotal(componentHeight);
        this.vBar.setIntervalDisplayed(paneHeight);
        this.vBar.setIntervalOffset(this.vScrollPosition * (-1L));
        this.vBar.setRelativPosition(new Vector2d(//
                getSize().getWidth() - this.vBar.getSize().getWidth(), //
                0 //
        ));

        this.cachedDrawable.addDrawable(//
                this.vBar.getDrawable(), //
                this.vBar.getRelativPosition().getX(), //
                this.vBar.getRelativPosition().getY(), //
                1 //
        );
    }

    private void addScrollbarDeadSpace() {
        if (!isVBarVisible() || !isHBarVisible()) {
            return;
        }

        int vBarWidth = this.vBar.getSize().getWidth();
        int hBarHeight = this.hBar.getSize().getHeight();

        DrawableSketch bar = new DrawableSketchImpl(//
                this.scrollbarColor, //
                new Size(vBarWidth, hBarHeight)//
        );

        this.cachedDrawable.addDrawable(//
                bar.draw(), //
                getSize().getWidth() - vBarWidth, //
                getSize().getHeight() - hBarHeight, //
                1 //
        );
    }

    private boolean isVBarVisible() {
        if (this.showVBar == ScrollbarShowType.ALWAYS) {
            return true;
        }
        if (this.showVBar == ScrollbarShowType.NOT) {
            return false;
        }
        int componentHeight = this.component.getSize().getHeight();
        int paneHeight = getSize().getHeight();
        paneHeight -= this.hBar.getSize().getHeight();
        return paneHeight <= componentHeight;
    }

    private boolean isHBarVisible() {
        if (this.showHBar == ScrollbarShowType.ALWAYS) {
            return true;
        }
        if (this.showHBar == ScrollbarShowType.NOT) {
            return false;
        }
        int componentWidth = this.component.getSize().getWidth();
        int paneWidth = getSize().getWidth();
        paneWidth -= this.vBar.getSize().getWidth();
        return paneWidth <= componentWidth;
    }

    private void addHScrollbarToCachedDrawable() {
        if (!isHBarVisible()) {
            return;
        }

        int componentWidth = this.component.getSize().getWidth();
        int paneWidth = getSize().getWidth();
        this.hBar.setIntervalTotal(componentWidth);
        this.hBar.setIntervalDisplayed(paneWidth);
        this.hBar.setIntervalOffset(this.hScrollPosition * (-1L));
        this.hBar.setRelativPosition(new Vector2d(//
                0, //
                getSize().getHeight() - hBar.getSize().getHeight() //
        ));

        this.cachedDrawable.addDrawable(//
                this.hBar.getDrawable(), //
                this.hBar.getRelativPosition().getX(), //
                this.hBar.getRelativPosition().getY(), //
                1 //
        );
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size size) {
        this.cachedDrawable = null;
        setSize(size);
        resizeComponent();
        getDrawable();
    }

    @Override
    protected boolean isPositionOnComponent(Integer mouseX, Integer mouseY) {
        return !isClickOnVBar(mouseX) && !isClickOnHBar(mouseY);
    }

    private boolean isClickOnHBar(Integer mouseY) {
        return isHBarVisible() && mouseY > getSize().getHeight() - hBar.getSize().getHeight();
    }

    private boolean isClickOnVBar(Integer mouseX) {
        return isVBarVisible() && mouseX > getSize().getWidth() - vBar.getSize().getWidth();
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Scrollbar. Position ({},{}).", mouseX, mouseY);

        if (isClickOnVBar(mouseX)) {
            this.vBar.onMouseClick(//
                    mouseX - this.vBar.getRelativPosition().getX(), //
                    mouseY - this.vBar.getRelativPosition().getY(), //
                    button //
            );
        } else if (isClickOnHBar(mouseY)) {
            this.hBar.onMouseClick(//
                    mouseX - this.hBar.getRelativPosition().getX(), //
                    mouseY - this.hBar.getRelativPosition().getY(), //
                    button //
            );
        }

        redraw();
    }

    private void scrollbarToCallback(int offset, ScrollbarType type) {
        if (type == ScrollbarType.VERTICAL) {
            if (offset <= 0) {
                // scroll up
                this.vScrollPosition = 0;
            } else {
                // scroll down
                int componentHeight = component.getSize().getHeight();
                int paneHeight = getSize().getHeight();
                this.vScrollPosition = Math.max(//
                        Math.min(0, -1 * (componentHeight - paneHeight)), //
                        -offset //
                );
            }
        } else {
            if (offset < 0) {
                // scroll left
                this.hScrollPosition = 0;
            } else {
                // scroll right
                int componenttWidth = component.getSize().getWidth();
                int panetWidth = getSize().getWidth();
                this.hScrollPosition = Math.max(//
                        Math.min(0, -1 * (componenttWidth - panetWidth)), //
                        -offset //
                );
            }
        }
        redraw();
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        this.vBar.onMouseWhellMoved(mouseX, mouseY, direction);
        redraw();
    }

    @Override
    protected MouseDraggable onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Pressed on Border. Position ({},{}).", mouseX, mouseY);
        if (isClickOnVBar(mouseX)) {
            return this.vBar.onMousePressed(//
                    mouseX - this.vBar.getRelativPosition().getX(), //
                    mouseY - this.vBar.getRelativPosition().getY(), //
                    button //
            );
        } else if (isClickOnHBar(mouseY)) {
            return this.hBar.onMousePressed(//
                    mouseX - this.hBar.getRelativPosition().getX(), //
                    mouseY - this.hBar.getRelativPosition().getY(), //
                    button //
            );
        }
        return null;
    }

    @Override
    protected MouseDraggable onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Released on Border. Position ({},{}).", mouseX, mouseY);
        if (isClickOnVBar(mouseX)) {
            return this.vBar.onMouseReleased(//
                    mouseX - this.vBar.getRelativPosition().getX(), //
                    mouseY - this.vBar.getRelativPosition().getY(), //
                    button //
            );
        } else if (isClickOnHBar(mouseY)) {
            return this.hBar.onMouseReleased(//
                    mouseX - this.hBar.getRelativPosition().getX(), //
                    mouseY - this.hBar.getRelativPosition().getY(), //
                    button //
            );
        }
        return null;
    }
}
