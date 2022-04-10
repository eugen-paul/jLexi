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
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

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
    public Border(Glyph parent, GuiComponent component, Color borderColor, Color backgroundColor) {
        super(parent, component);
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;

        this.compositor = new CentralGlypthCompositor<>(backgroundColor);
        this.component.getGlyph().setParent(this);
        this.component.getGlyph().setRelativPosition(new Vector2d(borderSize, borderSize));
        resizeTo(Size.ZERO_SIZE);
    }

    private void resizeComponent() {
        if (component.isResizeble()) {
            component.resizeTo(//
                    Math.max(0, getSize().getWidth() - this.borderSize * 2), //
                    Math.max(0, getSize().getHeight() - this.borderSize * 2)//
            );
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.compositor.setBackgroundColor(backgroundColor);
        this.cachedDrawable = null;
    }

    @Override
    public Drawable getPixels() {
        if (cachedDrawable != null) {
            return cachedDrawable;
        }

        if (getSize().getHeight() <= borderSize * 2 //
                || getSize().getWidth() <= borderSize * 2 //
        ) {
            cachedDrawable = new DrawableImpl(generateBlackBorder(), getSize());
            return cachedDrawable;
        }

        Size childSize = new Size(//
                size.getWidth() - borderSize * 2, //
                size.getHeight() - borderSize * 2 //
        );

        List<Glyph> composedGlyphs = compositor.compose(List.of(component.getGlyph()).iterator(), childSize);

        Drawable childDraw = composedGlyphs.get(0).getPixels();

        int[] componentPixels = childDraw.getPixels();
        int[] borderPixels = new int[(int) getSize().compArea()];

        Arrays.fill(borderPixels, 0, (int) getSize().compArea(), backgroundColor.getARGB());

        if (borderSize > 0) {
            Arrays.fill(borderPixels, 0, getSize().getWidth() * borderSize, borderColor.getARGB());
        }

        ImageArrayHelper.copyRectangle(//
                componentPixels, //
                childDraw.getPixelSize(), //
                new Vector2d(0, 0), //
                childDraw.getPixelSize(), //
                borderPixels, //
                getSize(), //
                new Vector2d(borderSize, borderSize) //
        );

        if (borderSize > 0) {
            int targetPosition = getSize().getWidth() * borderSize - borderSize;
            for (int i = borderSize; i < getSize().getHeight() - borderSize + 1; i++) {
                Arrays.fill(borderPixels, targetPosition, targetPosition + borderSize * 2, borderColor.getARGB());
                targetPosition += getSize().getWidth();
            }
        }

        if (borderSize > 0) {
            Arrays.fill(//
                    borderPixels, //
                    borderPixels.length - 1 - getSize().getWidth() * borderSize, //
                    borderPixels.length, //
                    borderColor.getARGB()//
            );
        }

        cachedDrawable = new DrawableImpl(borderPixels, getSize());
        return cachedDrawable;
    }

    private int[] generateBlackBorder() {
        int[] responsePixels = new int[(int) getSize().compArea()];
        Arrays.fill(responsePixels, borderColor.getARGB());
        return responsePixels;
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
        getPixels();
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseX < BORDER_SIZE //
                || mouseX > getSize().getWidth() - BORDER_SIZE //
                || mouseY < BORDER_SIZE //
                || mouseY > getSize().getHeight() - BORDER_SIZE //
        ) {
            LOGGER.trace("Click on Border. Position ({},{}).", mouseX, mouseY);
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY,
                    mouseX - BORDER_SIZE, mouseY - BORDER_SIZE);
            component.onMouseClick(mouseX - BORDER_SIZE, mouseY - BORDER_SIZE, button);
        }
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKeyTyped(Character key) {
        component.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        component.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        component.onKeyReleased(keyCode);
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        // TODO get Pixels from cachedDrawable
        return component.getGlyph().getPixels(//
                new Vector2d(//
                        position.getX() - borderSize, //
                        position.getY() - borderSize//
                ), //
                size);
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        // TODO add Pixels to cachedDrawable
        cachedDrawable = null;
        if (parent != null) {
            parent.notifyRedraw(drawData, relativPosition.addNew(this.relativPosition), size);
        }
    }

}
