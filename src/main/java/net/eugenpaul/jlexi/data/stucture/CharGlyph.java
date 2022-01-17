package net.eugenpaul.jlexi.data.stucture;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NullIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

public class CharGlyph implements Glyph {

    @Getter
    @Setter
    private char c;
    private FontStorage fontStorage;

    private String fontName;
    private int style;
    private int size;

    private Drawable drawable = null;

    public CharGlyph(char c, FontStorage fontStorage) {
        this.c = c;
        this.fontStorage = fontStorage;

        this.fontName = FontStorage.DEFAULT_FONT_NAME;
        this.style = FontStorage.DEFAULT_STYLE;
        this.size = FontStorage.DEFAULT_FONT_SIZE;

        this.drawable = fontStorage.ofChar(//
                c, //
                fontName, //
                style, //
                size//
        );
    }

    @Override
    public Drawable getPixels() {
        return this.drawable;
    }

    @Override
    public Size getSize() {
        if (null == this.drawable) {
            return Size.ZERO_SIZE;
        }
        return drawable.getPixelSize();
    }

    @Override
    public boolean isIntersects(Point point) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(Glyph glyph) {
        // TODO Auto-generated method stub

    }

    @Override
    public Glyph child(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return NullIterator.getNullIterator();
    }

    @Override
    public Glyph parent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

}
