package net.eugenpaul.jlexi.component.border;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
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
public class Border extends GuiCompenentMonoGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(Border.class);

    private static final int BORDER_SIZE = 2;

    private Color borderColor;
    private Color backgroundColor;
    private int borderSize = BORDER_SIZE;

    @Setter(value = AccessLevel.PROTECTED)
    @Getter(value = AccessLevel.PROTECTED)
    private GlyphCompositor<Glyph> compositor;

    /**
     * C'tor
     * 
     * @param component component that will be bordered.
     */
    public Border(Glyph parent, GuiGlyph component, Color borderColor, Color backgroundColor) {
        super(parent, component);
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        this.compositor = new CentralGlypthCompositor<>(backgroundColor);
        this.component.setRelativPosition(new Vector2d(borderSize, borderSize));
        resizeTo(Size.ZERO_SIZE);
    }

    private void resizeComponent() {
        if (component.isResizeble()) {
            component.resizeTo(//
                    Math.max(0, getSize().getWidth() - this.borderSize * 2), //
                    Math.max(0, getSize().getHeight() - this.borderSize * 2) //
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
        getDrawable();
    }

    @Override
    protected boolean isPositionOnComponent(Integer mouseX, Integer mouseY) {
        return mouseX > BORDER_SIZE //
                && mouseX < getSize().getWidth() - BORDER_SIZE //
                && mouseY > BORDER_SIZE //
                && mouseY < getSize().getHeight() - BORDER_SIZE //
        ;
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
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
