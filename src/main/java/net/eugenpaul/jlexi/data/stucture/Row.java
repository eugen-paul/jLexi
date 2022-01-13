package net.eugenpaul.jlexi.data.stucture;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.window.Window;

public class Row implements Glyph {

    @Getter
    @Setter
    private int maxWidth;

    public Row(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public Drawable draw(Window window) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bounds getSize() {
        // TODO Auto-generated method stub
        return null;
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
    public Glyph parent() {
        // TODO Auto-generated method stub
        return null;
    }

}
