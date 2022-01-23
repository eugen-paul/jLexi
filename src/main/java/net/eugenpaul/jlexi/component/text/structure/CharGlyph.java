package net.eugenpaul.jlexi.component.text.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.GlyphIterator;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.MouseButton;

public class CharGlyph extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharGlyph.class);

    @Getter
    @Setter
    private Character c;
    private FontStorage fontStorage;

    private String fontName;
    private int style;
    private int fontSize;
    private Drawable drawable;

    public CharGlyph(Glyph parent, Character c, FontStorage fontStorage,
            NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);
        this.c = c;
        this.fontStorage = fontStorage;

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.style = FontStorage.DEFAULT_STYLE;
        this.fontSize = FontStorage.DEFAULT_FONT_SIZE;

        this.drawable = this.fontStorage.ofChar(//
                c, //
                fontName, //
                style, //
                fontSize//
        );

        setSize(this.drawable.getPixelSize());
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
        checker.visit(this);
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

    @Override
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on \"{}\"", c);
        return this.getTextPaneListElement();
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("\"{}\" notifyUpdate to parent", c);
        getParent().notifyUpdate(this);
    }
}
