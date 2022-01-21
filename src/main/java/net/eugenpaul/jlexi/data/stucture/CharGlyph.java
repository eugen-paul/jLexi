package net.eugenpaul.jlexi.data.stucture;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NullIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

public class CharGlyph extends TextPaneElement {

    @Getter
    @Setter
    private char c;
    private FontStorage fontStorage;

    private String fontName;
    private int style;
    private int fontSize;
    private Drawable drawable = null;

    public CharGlyph(Glyph parent, char c, FontStorage fontStorage) {
        super(parent);
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
    }

    @Override
    public Drawable getPixels() {
        return this.drawable;
    }

    @Override
    public Size getPreferredSize() {
        if (null == this.drawable) {
            return Size.ZERO_SIZE;
        }
        return drawable.getPixelSize();
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
}
