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
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class EndOfLine extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndOfLine.class);

    private String fontName;
    private int fontSize;

    public EndOfLine(Glyph parent, FontStorage fontStorage, NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        int[] pixels = new int[fontStorage.getMaxAscent(fontName, fontSize)];
        setSize(new Size(1, pixels.length));
        drawableWithoutEffects = new DrawableImpl(pixels, getSize());
    }

    @Override
    public Drawable getPixels() {
        cachedDrawable = new DrawableImpl(drawableWithoutEffects.getPixels().clone(),
                drawableWithoutEffects.getPixelSize());

        effectsList.stream().forEach(v -> v.editDrawable(cachedDrawable));

        return cachedDrawable;
    }

    @Override
    public NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY) {
        LOGGER.trace("Click on Text place holder");
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
    public boolean isEndOfLine() {
        return true;
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        return false;
    }

}
