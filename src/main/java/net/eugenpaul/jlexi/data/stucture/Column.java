package net.eugenpaul.jlexi.data.stucture;

import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

public class Column implements Glyph {

    private List<Row> rows;

    public Column() {
        rows = new ArrayList<>();
    }

    @Override
    public Drawable getPixels() {
        return null;
    }

    @Override
    public Size getSize() {
        return null;
    }

    @Override
    public boolean isIntersects(Point point) {
        return false;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        if (glyph instanceof Row) {
            rows.add(position, (Row) glyph);
        }
        throw new IllegalArgumentException("Only Rows cann be added to Column.");
    }

    @Override
    public void remove(Glyph glyph) {
        rows.remove(glyph);
    }

    @Override
    public Glyph child(int position) {
        return rows.get(position);
    }

    @Override
    public Glyph parent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // checker.check(this);
    }

}
