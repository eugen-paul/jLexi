package net.eugenpaul.jlexi.component.text.structure;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class TextPlaceHolder extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPlaceHolder.class);

    private String fontName;
    private int fontSize;
    private Drawable drawable = null;

    public TextPlaceHolder(Glyph parent, FontStorage fontStorage,
            NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        int[] pixels = new int[fontStorage.getMaxAscent(fontName, fontSize)];
        setSize(new Size(1, pixels.length));
        drawable = new DrawableImpl(pixels, getSize());
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
    public Drawable getPixels() {
        Drawable respoDrawable = new DrawableImpl(drawable.getPixels().clone(), drawable.getPixelSize());

        effectsList.stream().forEach(v -> v.editDrawable(respoDrawable));

        return respoDrawable;
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
        getParent().notifyUpdate(this);
    }

    @Override
    public boolean isPlaceHolder() {
        return true;
    }

}
