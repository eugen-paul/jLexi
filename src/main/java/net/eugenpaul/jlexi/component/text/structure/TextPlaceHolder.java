package net.eugenpaul.jlexi.component.text.structure;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class TextPlaceHolder extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPlaceHolder.class);

    private String fontName;
    private int fontSize;
    private Drawable drawableWithoutEffects;

    public TextPlaceHolder(Glyph parent, FontStorage fontStorage,
            NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        int[] pixels = new int[fontStorage.getMaxAscent(fontName, fontSize)];
        setSize(new Size(1, pixels.length));
        drawableWithoutEffects = new DrawableImpl(pixels, getSize());
    }

    @Override
    public void notifyRedraw(Glyph child, Vector2d position, Size size) {
        cached = false;
        parent.notifyRedraw(this, Vector2d.zero(), this.size);
    }

    @Override
    public Drawable getPixels() {
        if (cached) {
            return cachedDrawable;
        }

        Drawable respoDrawable = new DrawableImpl(drawableWithoutEffects.getPixels().clone(),
                drawableWithoutEffects.getPixelSize());

        effectsList.stream().forEach(v -> v.editDrawable(respoDrawable));

        cachedDrawable = respoDrawable;
        cached = true;

        return respoDrawable;
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        if (!cached) {
            getPixels();
        }

        if (position.getX() == 0 && position.getY() == 0 //
                && this.size.equals(size)) {
            return getPixels();
        }

        int[] pixels = new int[size.getWidth() * size.getHeight()];

        ImageArrayHelper.copyRectangle(//
                cachedDrawable.getPixels(), //
                cachedDrawable.getPixelSize(), //
                position, //
                size, //
                pixels, //
                size, //
                Vector2d.zero() //
        );

        return new DrawableImpl(pixels, size);
    }

    @Override
    public NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY) {
        LOGGER.trace("Click on End-Of-File");
        return this.getTextPaneListElement();
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return NULLITERATOR;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("End-Of-File notifyUpdate to parent");
        cached = false;
        getParent().notifyUpdate(this);
    }

    @Override
    public boolean isPlaceHolder() {
        return true;
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        return false;
    }

}
