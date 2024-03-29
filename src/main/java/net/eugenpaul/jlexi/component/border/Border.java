package net.eugenpaul.jlexi.component.border;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Glyph with a boarder.
 * 
 */
@Slf4j
public class Border extends GuiCompenentMonoGlyph {

    private Color borderColor;
    private Color backgroundColor;
    private int borderSize;

    @Setter(value = AccessLevel.PROTECTED)
    @Getter(value = AccessLevel.PROTECTED)
    private GlyphCompositor<Glyph> compositor;

    @NoArgsConstructor
    public static class BorderBuilder {
        private Color borderColor;
        private Color backgroundColor;
        private int borderSize;
        private Glyph parent;
        private GuiGlyph component;

        public BorderBuilder borderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public BorderBuilder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public BorderBuilder borderSize(int borderSize) {
            this.borderSize = borderSize;
            return this;
        }

        public BorderBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public BorderBuilder component(GuiGlyph component) {
            this.component = component;
            return this;
        }

        public BorderBuilderComponent getBuilderComponent() {
            BorderBuilderComponent response = new BorderBuilderComponent();
            response.borderColor = borderColor;
            response.backgroundColor = backgroundColor;
            response.borderSize = borderSize;
            response.parent = parent;

            return response;
        }

        public Border build() {
            return new Border(parent, component, borderColor, backgroundColor, borderSize);
        }
    }

    @NoArgsConstructor
    public static class BorderBuilderComponent {
        private Color borderColor;
        private Color backgroundColor;
        private int borderSize;
        private Glyph parent;
        private GuiGlyph component;

        public BorderBuilderComponent component(GuiGlyph component) {
            this.component = component;
            return this;
        }

        public BorderBuilderComponent parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public Border build() {
            return new Border(parent, component, borderColor, backgroundColor, borderSize);
        }
    }

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    private Border(Glyph parent, GuiGlyph component, Color borderColor, Color backgroundColor, int borderSize) {
        super(parent, component);
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.borderSize = borderSize;

        this.compositor = new CentralGlypthCompositor<>(this.backgroundColor);
        this.component.setRelativPosition(new Vector2d(this.borderSize, this.borderSize));
        addGuiChild(component);
        resizeTo(Size.ZERO_SIZE);
    }

    public static BorderBuilder builder() {
        return new BorderBuilder();
    }

    private void resizeComponent() {
        if (this.component.isResizeble()) {
            this.component.resizeTo(//
                    Math.max(0, getSize().getWidth() - this.borderSize * 2), //
                    Math.max(0, getSize().getHeight() - this.borderSize * 2) //
            );
        }
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        resizeComponent();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.compositor.setBackgroundColor(backgroundColor);
        this.cachedDrawable = null;
    }

    @Override
    public Drawable getDrawable() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        if (getSize().getHeight() <= borderSize * 2 //
                || getSize().getWidth() <= borderSize * 2 //
        ) {
            cachedDrawable = new DrawableSketchImpl(borderColor, getSize());
            return cachedDrawable.draw();
        }

        Size childSize = new Size(//
                getSize().getWidth() - borderSize * 2, //
                getSize().getHeight() - borderSize * 2 //
        );

        List<Glyph> composedGlyphs = compositor.compose(List.of((Glyph) component).iterator(), childSize);

        cachedDrawable = new DrawableSketchImpl(backgroundColor);
        var childDraw = composedGlyphs.get(0).getDrawable();

        cachedDrawable.addDrawable(childDraw, borderSize, borderSize, 0);

        int[] borderPixelsVertical = new int[getSize().getWidth() * borderSize];
        Arrays.fill(borderPixelsVertical, 0, borderPixelsVertical.length, borderColor.getArgb());

        DrawablePixelsImpl borderVertical = DrawablePixelsImpl.builderArgb()//
                .argbPixels(borderPixelsVertical)//
                .size(new Size(getSize().getWidth(), borderSize))//
                .build();

        cachedDrawable.addDrawable(borderVertical, 0, 0, 1);
        cachedDrawable.addDrawable(borderVertical, 0, getSize().getHeight() - borderSize, 1);

        int[] borderPixelsHorizontal = new int[getSize().getHeight() * borderSize];
        Arrays.fill(borderPixelsHorizontal, 0, borderPixelsHorizontal.length, borderColor.getArgb());

        DrawablePixelsImpl borderHorizontal = DrawablePixelsImpl.builderArgb()//
                .argbPixels(borderPixelsHorizontal)//
                .size(new Size(borderSize, getSize().getHeight()))//
                .build();

        cachedDrawable.addDrawable(borderHorizontal, 0, 0, 1);
        cachedDrawable.addDrawable(borderHorizontal, getSize().getWidth() - borderSize, 0, 1);

        return cachedDrawable.draw();
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
    }

    @Override
    protected boolean isPositionOnComponent(Integer mouseX, Integer mouseY) {
        return mouseX > borderSize //
                && mouseX < getSize().getWidth() - borderSize //
                && mouseY > borderSize //
                && mouseY < getSize().getHeight() - borderSize //
        ;
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
    }

    @Override
    protected MouseDraggable onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Pressed on Border. Position ({},{}).", mouseX, mouseY);
        return null;
    }

    @Override
    protected MouseDraggable onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Released on Border. Position ({},{}).", mouseX, mouseY);
        return null;
    }
}
