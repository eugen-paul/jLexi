package net.eugenpaul.jlexi.data.stucture;

import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.window.Window;

public class Column implements Glyph {

    private List<Row> rows;

    public Column() {
        rows = new ArrayList<>();
    }

    @Override
    public Drawable draw(Window window) {
        return null;
    }

    @Override
    public Bounds getSize() {
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

}
