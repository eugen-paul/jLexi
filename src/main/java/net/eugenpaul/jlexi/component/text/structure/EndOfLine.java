package net.eugenpaul.jlexi.component.text.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.GlyphIterator;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.MouseButton;

public class EndOfLine extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndOfLine.class);

    private String fontName;
    private int fontSize;
    private Drawable drawable = null;

    public EndOfLine(Glyph parent, FontStorage fontStorage, NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        int[] pixels = new int[fontStorage.getMaxAscent(fontName, fontSize)];
        setSize(new Size(1, pixels.length));
        drawable = new DrawableImpl(pixels, getSize());
    }

    @Override
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Text place holder");
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
    public GlyphIterator createIterator() {
        return NullIterator.getNullIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("Text place holder notifyUpdate to parent");
        getParent().notifyUpdate(this);
    }

    @Override
    public boolean isEndOfLine() {
        return true;
    }

}
