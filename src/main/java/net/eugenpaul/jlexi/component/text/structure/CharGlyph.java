package net.eugenpaul.jlexi.component.text.structure;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class CharGlyph extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharGlyph.class);

    @Getter
    private Character c;

    private FontStorage fontStorage;

    private String fontName;
    private int style;
    private int fontSize;

    public CharGlyph(Glyph parent, Character c, FontStorage fontStorage,
            NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);
        this.c = c;
        this.fontStorage = fontStorage;

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.style = FontStorage.DEFAULT_STYLE;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        this.drawableWithoutEffects = this.fontStorage.ofChar(//
                c, //
                fontName, //
                style, //
                fontSize//
        );

        setSize(this.drawableWithoutEffects.getPixelSize());

    }

    @Override
    public Drawable getPixels() {
        cachedDrawable = new DrawableImpl(drawableWithoutEffects.getPixels().clone(),
                drawableWithoutEffects.getPixelSize());

        effectsList.stream().forEach(v -> v.editDrawable(cachedDrawable));

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return NULLITERATOR;
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

    @Override
    public NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY) {
        LOGGER.trace("Click on \"{}\"", c);
        return this.getTextPaneListElement();
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        return false;
    }

}
