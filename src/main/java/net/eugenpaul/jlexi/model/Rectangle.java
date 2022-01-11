package net.eugenpaul.jlexi.model;

import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.data.Bounds;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Point;

public class Rectangle implements Glyph {

    private Bounds bounds;
    private List<Glyph> children;
    private Glyph parent;

    public Rectangle(int p1X, int p1Y, int p2X, int p2Y) {
        this.bounds = new Bounds(p1X, p1Y, p2X, p2Y);
        this.children = new ArrayList<>();
        parent = null;
    }

    @Override
    public Drawable draw() {
        return null;
    }

    @Override
    public Bounds getSize() {
        return new Bounds(this.bounds);
    }

    @Override
    public boolean isIntersects(Point point) {
        return bounds.getP1X() <= point.getX()//
                && bounds.getP1Y() <= point.getY()//
                && bounds.getP2X() >= point.getX()//
                && bounds.getP2Y() >= point.getY()//
        ;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        if (children.size() - 1 > position) {
            children.add(position, glyph);
        } else if (position > 0) {
            children.add(children.size() - 1, glyph);
        } else {
            children.add(0, glyph);
        }
    }

    @Override
    public void remove(Glyph glyph) {
        // TODO Auto-generated method stub

    }

    @Override
    public Glyph child(int position) {
        return null;
    }

    @Override
    public Glyph parent() {
        // TODO Auto-generated method stub
        return parent;
    }

}
