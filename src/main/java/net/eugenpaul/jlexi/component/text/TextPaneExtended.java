package net.eugenpaul.jlexi.component.text;

import java.util.Collections;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextStructureFormToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocument;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Display Rows.
 */
public class TextPaneExtended extends Glyph implements GuiComponent, KeyHandlerable, TextUpdateable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneExtended.class);

    private TextPaneDocument document;

    private TextCompositor<TextStructureForm> compositor;

    @Getter
    private FontStorage fontStorage;

    public TextPaneExtended(Glyph parent, FontStorage fontStorage, EffectHandler effectWorker) {
        super(parent);
        this.fontStorage = fontStorage;
        this.compositor = new TextStructureFormToColumnCompositor();
        this.document = new TextPaneDocument(new FormatAttribute(), fontStorage, "Hello, World!");

        resizeTo(Size.ZERO_SIZE);
    }

    @Override
    public Drawable getPixels() {
        var sites = compositor.compose(document.getRows(getSize()).iterator(), getSize());

        int sizeW = 0;
        int sizeH = 0;

        for (var el : sites) {
            sizeW = Math.max(sizeW, el.getSize().getWidth());
            sizeH += el.getSize().getHeight();
        }

        Size pixelSize = new Size(sizeW, sizeH);
        int[] pixels = new int[sizeW * sizeH];

        cachedDrawable = new DrawableImpl(pixels, pixelSize);

        Vector2d position = new Vector2d(0, 0);
        for (var el : sites) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);
            position.setY(position.getY() + el.getSize().getHeight());
        }

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);
    }

    @Override
    public void setText(String text) {
        LOGGER.trace("Set Document.text to \"{}\".", text);
        document = new TextPaneDocument(new FormatAttribute(), fontStorage, text);
        getPixels();
    }

    @Override
    public void resizeTo(Size size) {
        setSize(size);
        cachedDrawable = null;
        document.resetStructure();
    }

    @Override
    public void onKeyTyped(Character key) {
        LOGGER.trace("Key typed: {}", key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        LOGGER.trace("Key pressed: {}", keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        LOGGER.trace("Key released: {}", keyCode);
    }

    @Override
    public Glyph getThis() {
        return this;
    }

    @Override
    public void doCursorMove(CursorMove cursorMove) {
        LOGGER.trace("do cursor move: {}", cursorMove);
    }

    @Override
    public void keyUpdate() {
        parent.notifyRedraw(getPixels(), relativPosition, size);
    }

    @Override
    public EffectHandler getEffectHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getMouseCursor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMouseCursor(Cursor cursor) {
        // TODO Auto-generated method stub

    }

}
