package net.eugenpaul.jlexi.component.scrollpane;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.ScrollGlypthCompositor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImageImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.draw.DrawableImageImpl.DrawableImageImplBufferBuilder;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

/**
 * Glyph with a Scrollpane.
 */
public class Scrollpane extends GuiCompenentMonoGlyph {

    private static final String ARROW_UP_DEFAULT = "src/main/resources/textures/gui/arrow_up.jpg";
    private static final String ARROW_DOWN_DEFAULT = "src/main/resources/textures/gui/arrow_down.jpg";
    private static final String ARROW_LEFT_DEFAULT = "src/main/resources/textures/gui/arrow_left.jpg";
    private static final String ARROW_RIGHT_DEFAULT = "src/main/resources/textures/gui/arrow_right.jpg";

    private static final Logger LOGGER = LoggerFactory.getLogger(Scrollpane.class);

    private static final int V_BAR_WIDTH = 11;
    private static final int H_BAR_HEIGHT = 11;

    private Color scrollbarColor;
    private Color backgroundColor;

    private ScrollGlypthCompositor<Glyph> compositor;

    private int vBarWidth = V_BAR_WIDTH;
    private int hBarHeight = H_BAR_HEIGHT;

    private ScrollBarShow showVBar = ScrollBarShow.AT_NEED;
    private ScrollBarShow showHBar = ScrollBarShow.AT_NEED;

    private int vScrollPosition = 0;
    private int hScrollPosition = 0;

    private DrawableImageImplBufferBuilder arrowUpBuffer;
    private DrawableImageImplBufferBuilder arrowDownBuffer;
    private DrawableImageImplBufferBuilder arrowLeftBuffer;
    private DrawableImageImplBufferBuilder arrowRightBuffer;

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

        private ScrollBarShow showVBar = ScrollBarShow.AT_NEED;
        private ScrollBarShow showHBar = ScrollBarShow.AT_NEED;

        private String arrowUpPath = ARROW_UP_DEFAULT;
        private String arrowDownPath = ARROW_DOWN_DEFAULT;
        private String arrowLeftPath = ARROW_LEFT_DEFAULT;
        private String arrowRightPath = ARROW_RIGHT_DEFAULT;

        private ScrollpaneBuilder() {

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

        public ScrollpaneBuilder arrowUpPath(String arrowUpPath) {
            this.arrowUpPath = arrowUpPath;
            return this;
        }

        public ScrollpaneBuilder arrowDownPath(String arrowDownPath) {
            this.arrowDownPath = arrowDownPath;
            return this;
        }

        public ScrollpaneBuilder arrowLeftPath(String arrowLeftPath) {
            this.arrowLeftPath = arrowLeftPath;
            return this;
        }

        public ScrollpaneBuilder arrowRightPath(String arrowRightPath) {
            this.arrowRightPath = arrowRightPath;
            return this;
        }

        public Scrollpane build() {
            Scrollpane response = new Scrollpane(parent, component, scrollbarColor, backgroundColor);

            initArrows(response);

            response.showVBar = showVBar;
            response.showHBar = showHBar;

            return response;
        }

        private void initArrows(Scrollpane scrollPane) {
            try {
                scrollPane.arrowUpBuffer = DrawableImageImpl.builder().fromPath(Paths.get(arrowUpPath));
                scrollPane.arrowDownBuffer = DrawableImageImpl.builder().fromPath(Paths.get(arrowDownPath));
                scrollPane.arrowLeftBuffer = DrawableImageImpl.builder().fromPath(Paths.get(arrowLeftPath));
                scrollPane.arrowRightBuffer = DrawableImageImpl.builder().fromPath(Paths.get(arrowRightPath));
            } catch (IOException e) {
                scrollPane.arrowUpBuffer = null;
                scrollPane.arrowDownBuffer = null;
                scrollPane.arrowLeftBuffer = null;
                scrollPane.arrowRightBuffer = null;
                LOGGER.error("Cann't load arrow image", e);
            }
        }
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

        if (!isHBarVisible()) {
            hScrollPosition = 0;
        }

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
        if (showVBar == ScrollBarShow.ALWAYS) {
            return true;
        }
        if (showVBar == ScrollBarShow.NOT) {
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

        int barHeight = compVBarHeight();

        DrawableSketch bar = new DrawableSketchImpl(//
                scrollbarColor, //
                new Size(//
                        this.vBarWidth, //
                        barHeight//
                )//
        );

        if (arrowUpBuffer != null) {
            Drawable arrowUp = arrowUpBuffer//
                    .size(new Size(this.vBarWidth, this.vBarWidth))//
                    .build();
            bar.addDrawable(arrowUp, 0, 0, 0);
        }

        if (arrowDownBuffer != null) {
            Drawable arrowDown = arrowDownBuffer//
                    .size(new Size(this.vBarWidth, this.vBarWidth))//
                    .build();
            bar.addDrawable(arrowDown, 0, barHeight - this.vBarWidth, 0);
        }

        this.cachedDrawable.addDrawable(bar.draw(), getSize().getWidth() - this.vBarWidth, 0, 1);
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
        if (showHBar == ScrollBarShow.ALWAYS) {
            return true;
        }
        if (showHBar == ScrollBarShow.NOT) {
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

        int barWidth = compHBarWidth();

        DrawableSketch bar = new DrawableSketchImpl(//
                scrollbarColor, //
                new Size(//
                        barWidth, //
                        this.hBarHeight //
                )//
        );

        if (arrowLeftBuffer != null) {
            Drawable arrowLeft = arrowLeftBuffer//
                    .size(new Size(this.hBarHeight, this.hBarHeight))//
                    .build();
            bar.addDrawable(arrowLeft, 0, 0, 0);
        }

        if (arrowRightBuffer != null) {
            Drawable arrowRight = arrowRightBuffer//
                    .size(new Size(this.vBarWidth, this.vBarWidth))//
                    .build();
            bar.addDrawable(arrowRight, barWidth - this.hBarHeight, 0, 0);
        }

        this.cachedDrawable.addDrawable(bar.draw(), 0, getSize().getHeight() - this.hBarHeight, 1);
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
